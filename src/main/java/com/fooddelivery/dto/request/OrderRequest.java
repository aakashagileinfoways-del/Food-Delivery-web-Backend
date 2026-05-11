package com.fooddelivery.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {

    private Long restaurantId;
    private List<OrderItemRequest> items;

    private String deliveryAddress;
    private Double deliveryLatitude; // optional - used for closest agent calculation
    private Double deliveryLongitude; // optional - used for closest agent calculation
    private String paymentType;
    private String instructions;
}