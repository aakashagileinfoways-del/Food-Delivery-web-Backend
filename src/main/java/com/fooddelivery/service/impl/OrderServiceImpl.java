package com.fooddelivery.service.impl;

import com.fooddelivery.dto.request.OrderItemRequest;
import com.fooddelivery.dto.request.OrderRequest;
import com.fooddelivery.dto.response.OrderItemResponse;
import com.fooddelivery.dto.response.OrderResponse;
import com.fooddelivery.entity.*;
import com.fooddelivery.enums.OrderStatus;
import com.fooddelivery.enums.RoleType;
import com.fooddelivery.enums.RestaurantStatus;
import com.fooddelivery.repository.*;
import com.fooddelivery.service.OrderService;
import com.fooddelivery.service.DeliveryService;
// import com.fooddelivery.service.EmailService;
import com.fooddelivery.service.PaymentService;
// import com.fooddelivery.service.EmailService;
// import com.fooddelivery.util.EmailTemplateUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.fooddelivery.kafka.event.OrderEvent;
import com.fooddelivery.kafka.event.PaymentEvent;
import com.fooddelivery.kafka.producer.KafkaEventPublisher;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final DeliveryService deliveryService;
    private final PaymentService paymentService;
    // private final EmailService emailService;
    private final KafkaEventPublisher kafkaEventPublisher;

    @Transactional
    @Override
    public String placeOrder(OrderRequest request, String customerEmail) {
        // 1. Fetch customer
        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // 2. Fetch restaurant
        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        if (restaurant.getStatus() != RestaurantStatus.APPROVED) {
            throw new RuntimeException("Restaurant is not approved for orders");
        }

        User user = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole().getName() != RoleType.CUSTOMER) {
            System.out.println("Logged user role: " + user.getRole().getName());
            throw new RuntimeException("Only customers can place orders");
        }
        // 3. Create order
        Order order = new Order();
        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setDeliveryAddress(request.getDeliveryAddress());
        order.setDeliveryLatitude(request.getDeliveryLatitude());
        order.setDeliveryLongitude(request.getDeliveryLongitude());
        order.setPaymentType(request.getPaymentType());
        order.setInstructions(request.getInstructions());
        order.setStatus(OrderStatus.PLACED);
        order.setOrderTime(LocalDateTime.now());

        double total = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        // 4. Loop items
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new RuntimeException("Order must contain at least one item");
        }
        for (OrderItemRequest itemRequest : request.getItems()) {
            if (itemRequest.getQuantity() == null || itemRequest.getQuantity() <= 0) {
                throw new RuntimeException("Invalid quantity for item");
            }

            MenuItem menu = menuRepository.findById(itemRequest.getMenuId())
                    .orElseThrow(() -> new RuntimeException("Menu item not found"));

            if (menu.getRestaurant() == null || !menu.getRestaurant().getId().equals(request.getRestaurantId())) {
                throw new RuntimeException("Menu item does not belong to the selected restaurant");
            }

            if (menu.getAvailable() == null || !menu.getAvailable()) {
                throw new RuntimeException("Selected menu item is not available");
            }

            OrderItem item = new OrderItem();
            item.setMenu(menu);
            item.setQuantity(itemRequest.getQuantity());

            double price = menu.getPrice() * itemRequest.getQuantity();
            item.setPrice(price);
            item.setOrder(order);

            total += price;
            orderItems.add(item);
        }

        // 5. Set items & total
        order.setItems(orderItems);
        order.setTotalAmount(total);

        // 6. Mock payment - if payment fails, nothing is persisted because we're still
        // inside @Transactional
        // paymentService.processPayment(request.getPaymentType(), total,
        // customerEmail);
        String txnId = paymentService.processPayment(
                request.getPaymentType(),
                total,
                customerEmail);

        order.setTransactionId(txnId);
        // 7. Save
        orderRepository.save(order);

        kafkaEventPublisher.publishOrder(
                new OrderEvent(
                        order.getId(),
                        "CREATED",
                        "Order placed successfully",
                        LocalDateTime.now(),
                        order.getCustomer().getEmail()));
        System.out.println("Order placed with ID: " + order.getId());

        kafkaEventPublisher.publishPayment(
                new PaymentEvent(
                        order.getId(),
                        "PAYMENT_PROCESSED",
                        "Payment successful with txn ID: " + txnId));

        System.out.println("Order placed with ID: " + order.getId());

        // try {
        // String html = EmailTemplateUtil.buildOrderEmail(order);
        // emailService.sendHtmlEmail(
        // order.getCustomer().getEmail(),
        // "Order Confirmation #" + order.getId(),
        // html);
        // } catch (Exception e) {
        // System.out.println("Email failed but order placed: " + e.getMessage());
        // }

        return "Order placed successfully";

    }

    @Override
    public void updateOrderStatus(Long orderId, OrderStatus status, String actorRole) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        RoleType role = RoleType.valueOf(actorRole);
        validateStatusTransition(order.getStatus(), status, role);

        // 🚚 Assign agent when OUT_FOR_DELIVERY
        if (status == OrderStatus.OUT_FOR_DELIVERY && order.getDeliveryAgent() == null) {
            var agent = deliveryService.assignAgent(order.getDeliveryLatitude(), order.getDeliveryLongitude());
            deliveryService.markBusy(agent);
            order.setDeliveryAgent(agent);
        }

        // ✅ ⏱️ SET STAGE TIMESTAMPS (IMPORTANT PART)
        if (status == OrderStatus.CONFIRMED && order.getConfirmedTime() == null) {
            order.setConfirmedTime(LocalDateTime.now());
        }

        if (status == OrderStatus.IN_KITCHEN && order.getKitchenStartTime() == null) {
            order.setKitchenStartTime(LocalDateTime.now());
        }

        if (status == OrderStatus.OUT_FOR_DELIVERY && order.getOutForDeliveryTime() == null) {
            order.setOutForDeliveryTime(LocalDateTime.now());
        }

        // 🚚 Release agent on delivery
        if (status == OrderStatus.DELIVERED && order.getDeliveryAgent() != null) {
            deliveryService.markAvailable(order.getDeliveryAgent());
        }

        // 🚫 Release agent on cancel
        if (status == OrderStatus.CANCELLED && order.getDeliveryAgent() != null) {
            deliveryService.markAvailable(order.getDeliveryAgent());
        }

        // ✅ Final status update
        order.setStatus(status);

        orderRepository.save(order);
    }

    private void validateStatusTransition(OrderStatus current, OrderStatus target, RoleType actorRole) {
        if (target == OrderStatus.CANCELLED && current == OrderStatus.DELIVERED) {
            throw new RuntimeException("Cannot cancel a delivered order");
        }

        // Role-based constraints
        if (actorRole == RoleType.RESTAURANT_OWNER) {
            if (!(target == OrderStatus.CONFIRMED
                    || target == OrderStatus.IN_KITCHEN
                    || target == OrderStatus.OUT_FOR_DELIVERY
                    || target == OrderStatus.CANCELLED)) {
                throw new RuntimeException("Restaurant owner cannot move order to this status");
            }
        } else if (actorRole == RoleType.DELIVERY_AGENT) {
            if (!(target == OrderStatus.DELIVERED || target == OrderStatus.CANCELLED)) {
                throw new RuntimeException("Delivery agent cannot move order to this status");
            }
        } else if (actorRole != RoleType.ADMIN) {
            throw new RuntimeException("Unauthorized role for status update");
        }

        // State transition constraints
        boolean valid = (current == OrderStatus.PLACED && target == OrderStatus.CONFIRMED) ||
                (current == OrderStatus.CONFIRMED && target == OrderStatus.IN_KITCHEN) ||
                (current == OrderStatus.IN_KITCHEN && target == OrderStatus.OUT_FOR_DELIVERY) ||
                (current == OrderStatus.OUT_FOR_DELIVERY && target == OrderStatus.DELIVERED) ||
                (target == OrderStatus.CANCELLED && current != OrderStatus.DELIVERED);

        if (!valid) {
            throw new RuntimeException("Invalid status transition from " + current + " to " + target);
        }
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    // @Override
    // public OrderResponse getOrderDetails(Long id) {
    // Order order = orderRepository.findById(id)
    // .orElseThrow(() -> new RuntimeException("Order not found"));

    // return new OrderResponse(
    // order.getId(),
    // order.getStatus().name(),
    // order.getTotalAmount(),
    // order.getOrderTime(),
    // order.getRestaurant().getName(),
    // order.getItems().stream()
    // .map(item -> new OrderItemResponse(
    // item.getId(),
    // item.getMenu().getName(),
    // item.getQuantity(),
    // item.getPrice()))
    // .toList());
    // }

    @Override
    public OrderResponse getOrderDetails(Long id) {

        String key = "order:" + id;

        try {
            String cachedData = redisTemplate.opsForValue().get(key);

            if (cachedData != null) {
                System.out.println("🔥 Data from Redis");
                return objectMapper.readValue(cachedData, OrderResponse.class);
            }

            System.out.println("💾 Data from DB");

            Order order = orderRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            OrderResponse response = new OrderResponse(
                    order.getId(),
                    order.getStatus().name(),
                    order.getTotalAmount(),
                    order.getOrderTime(),
                    order.getRestaurant().getName(),
                    order.getItems().stream()
                            .map(item -> new OrderItemResponse(
                                    item.getId(),
                                    item.getMenu().getName(),
                                    item.getQuantity(),
                                    item.getPrice()))
                            .toList());

            String json = objectMapper.writeValueAsString(response);
            redisTemplate.opsForValue().set(key, json, 10, TimeUnit.MINUTES);

            return response;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<OrderResponse> getOrdersByUser(String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("User role in service: " + user.getRole().getName() + " for email: " + userEmail);
        // 🔥 Optional but important
        if (user.getRole().getName() != RoleType.CUSTOMER) {
            throw new RuntimeException("Only customers can view orders");
        }

        List<Order> orders = orderRepository.findByCustomerId(user.getId());

        return orders.stream()
                .map(order -> new OrderResponse(
                        order.getId(),
                        order.getStatus().name(),
                        order.getTotalAmount(),
                        order.getOrderTime(),
                        order.getRestaurant().getName(),
                        order.getItems().stream()
                                .map(item -> new OrderItemResponse(
                                        item.getId(),
                                        item.getMenu().getName(),
                                        item.getQuantity(),
                                        item.getPrice()))
                                .toList()))
                .toList();

    }

    @Override
    public void deliverOrder(Long orderId) {
        updateOrderStatus(orderId, OrderStatus.DELIVERED, RoleType.DELIVERY_AGENT.name());
    }

    // public Order placeorder(Order order) {
    // // TODO Auto-generated method stub
    // throw new UnsupportedOperationException("Unimplemented method 'placeorder'");
    // }

}