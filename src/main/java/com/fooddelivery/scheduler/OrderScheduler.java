package com.fooddelivery.scheduler;

import com.fooddelivery.entity.Order;
import com.fooddelivery.enums.OrderStatus;
import com.fooddelivery.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderScheduler {

    private final OrderRepository orderRepository;

    @Scheduled(fixedRate = 300000) // every 5 minutes
    public void checkDelayedOrders() {

        List<Order> orders = orderRepository.findAll();

        for (Order order : orders) {

            LocalDateTime now = LocalDateTime.now();

            // 🚨 Acceptance Delay
            if (order.getStatus() == OrderStatus.PLACED) {
                long minutes = Duration.between(order.getOrderTime(), now).toMinutes();
                if (minutes > 1) {
                    System.out.println("⚠️ Acceptance delay: Order " + order.getId());
                }
            }

            // 🍳 Cooking Delay
            if (order.getStatus() == OrderStatus.IN_KITCHEN && order.getKitchenStartTime() != null) {
                long minutes = Duration.between(order.getKitchenStartTime(), now).toMinutes();
                if (minutes > 20) {
                    System.out.println("🍳 Cooking delay: Order " + order.getId());
                }
            }

            // 🚚 Delivery Delay
            if (order.getStatus() == OrderStatus.OUT_FOR_DELIVERY && order.getOutForDeliveryTime() != null) {
                long minutes = Duration.between(order.getOutForDeliveryTime(), now).toMinutes();
                if (minutes > 40) {
                    System.out.println("🚚 Delivery delay: Order " + order.getId());
                }
            }
        }
    }
}