package com.fooddelivery.service.impl;

import com.fooddelivery.dto.response.AdminDashboardResponse;
import com.fooddelivery.enums.OrderStatus;
import com.fooddelivery.enums.RestaurantStatus;
import com.fooddelivery.repository.OrderRepository;
import com.fooddelivery.repository.RestaurantRepository;
import com.fooddelivery.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    public AdminDashboardResponse getDashboard() {

        AdminDashboardResponse res = new AdminDashboardResponse();

        // Orders
        res.setTotalOrders(orderRepository.count());
        res.setPendingOrders(orderRepository.countByStatus(OrderStatus.PLACED));
        res.setDeliveredOrders(orderRepository.countByStatus(OrderStatus.DELIVERED));
        res.setCancelledOrders(orderRepository.countByStatus(OrderStatus.CANCELLED));

        // Revenue
        res.setTotalRevenue(orderRepository.getTotalRevenue());

        // Delivery time
        res.setAvgDeliveryTime(orderRepository.getAvgDeliveryTime());

        // Restaurants
        res.setActiveRestaurants(
                restaurantRepository.countByStatus(RestaurantStatus.APPROVED)
        );

        return res;
    }
}