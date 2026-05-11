package com.fooddelivery.dto.request;

import lombok.Data;

@Data
public class RestaurantRequest {
    private String name;
    private String cuisineType;
    private String address;
}