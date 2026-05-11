package com.fooddelivery.service;

import java.util.List;

import com.fooddelivery.dto.request.OrderRequest;
import com.fooddelivery.dto.response.OrderResponse;
import com.fooddelivery.enums.OrderStatus;

public interface OrderService {
    String placeOrder(OrderRequest request, String customerEmail);

    void updateOrderStatus(Long id, OrderStatus status, String actorRole);

    com.fooddelivery.entity.Order getOrderById(Long id);

    OrderResponse getOrderDetails(Long id);

    List<OrderResponse> getOrdersByUser(String userEmail);

    void deliverOrder(Long orderId);

}