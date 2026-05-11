package com.fooddelivery.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;


@Data
public class OrderResponse {

    private Long orderId;
    private String status;
    private Double totalAmount;
    private LocalDateTime createdAt;
    private String restaurantName;
     private List<OrderItemResponse> items;


    public OrderResponse(Long orderId, String status, Double totalAmount, LocalDateTime createdAt, String restaurantName , List<OrderItemResponse> items) {
        this.orderId = orderId;
        this.status = status;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.restaurantName = restaurantName;
        this.items = items;

    }
}