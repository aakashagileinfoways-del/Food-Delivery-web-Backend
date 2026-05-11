package com.fooddelivery.service.impl;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fooddelivery.dto.request.OrderItemRequest;
import com.fooddelivery.dto.request.OrderRequest;
import com.fooddelivery.entity.MenuItem;
import com.fooddelivery.entity.Order;
import com.fooddelivery.entity.Restaurant;
import com.fooddelivery.entity.Role;
import com.fooddelivery.entity.User;
import com.fooddelivery.enums.RestaurantStatus;
import com.fooddelivery.enums.RoleType;
import com.fooddelivery.repository.MenuRepository;
import com.fooddelivery.repository.OrderRepository;
import com.fooddelivery.repository.RestaurantRepository;
import com.fooddelivery.repository.UserRepository;
import com.fooddelivery.service.DeliveryService;
import com.fooddelivery.service.EmailService;
import com.fooddelivery.service.PaymentService;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private RestaurantRepository restaurantRepository;
    @Mock private MenuRepository menuRepository;
    @Mock private OrderRepository orderRepository;
    @Mock private DeliveryService deliveryService;
    @Mock private PaymentService paymentService;
    @Mock private EmailService emailService;

    @InjectMocks
    private OrderServiceImpl orderService;
 @Test
void testGetOrderById() {

    Order order = new Order();
    order.setId(1L);

    Mockito.when(orderRepository.findById(1L))
            .thenReturn(Optional.of(order));

    Order result = orderService.getOrderById(1L);

    Assertions.assertThat(1L).isEqualTo(result.getId());
}




@Test
void testPlaceOrder() {

    // mock user
    User user = new User();
    Role role = new Role();
    role.setName(RoleType.CUSTOMER);
    user.setRole(role);

    Mockito.when(userRepository.findByEmail("test@mail.com"))
            .thenReturn(Optional.of(user));

    // mock restaurant
    Restaurant restaurant = new Restaurant();
    restaurant.setId(1L);
    restaurant.setStatus(RestaurantStatus.APPROVED);

    Mockito.when(restaurantRepository.findById(1L))
            .thenReturn(Optional.of(restaurant));

    // mock menu
    MenuItem menu = new MenuItem();
    menu.setId(10L);
    menu.setPrice(100.0);
    menu.setAvailable(true);
    menu.setRestaurant(restaurant);

    Mockito.when(menuRepository.findById(10L))
            .thenReturn(Optional.of(menu));

    // mock payment
    Mockito.when(paymentService.processPayment(
            Mockito.any(),
            Mockito.anyDouble(),
            Mockito.anyString()))
            .thenReturn("TXN123");

    // request
    OrderItemRequest item = new OrderItemRequest();
    item.setMenuId(10L);
    item.setQuantity(2);

    OrderRequest request = new OrderRequest();
    request.setRestaurantId(1L);
    request.setItems(List.of(item));
    request.setDeliveryAddress("Ahmedabad");

    Mockito.when(orderRepository.save(Mockito.any()))
            .thenAnswer(inv -> inv.getArgument(0));

    // ACT
    String result = orderService.placeOrder(request, "test@mail.com");

    // ASSERT
    Assertions.assertThat(result).isEqualTo("Order placed successfully");
}





@Test
void testgetOrderDetails() {


    Order order = new Order();
    order.setId(1L);

    Mockito.when(orderRepository.findById(1L))
            .thenReturn(Optional.of(order));

    var result = orderService.getOrderDetails(1L);

    Assertions.assertThat(result.getOrderId()).isEqualTo(1L);
}






}

// @Test
// void placeorder() {
//         Order order = new Order();
//         order.setId(1L);
    
//         Mockito.when(orderRepository.save(order))
//                 .thenReturn(order);
    
//         Order result = orderService.placeorder(order);
    
//         Assertions.assertThat(1L).isEqualTo(result.getId());
// }}

// @Test
// void testGetOrderById_Null() {

//     Mockito.when(orderRepository.findById(1L))
//             .thenReturn(Optional.empty());

//     Order result = orderService.getOrderById(1L);

//     Assertions.assertThat(result).isNull();
// }




// @Test 
// void testGetOrderById_NotFound() {

//     Mockito.when(orderRepository.findById(1L))
//             .thenReturn(Optional.empty());

//     Order result = orderService.getOrderById(1L);

//     Assertions.assertThat(result).isNull();
// }
   

