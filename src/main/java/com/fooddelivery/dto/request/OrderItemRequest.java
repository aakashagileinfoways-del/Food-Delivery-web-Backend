package com.fooddelivery.dto.request;

import lombok.Data;

@Data
public class OrderItemRequest {
    private Long menuId;
    private Integer quantity;
}