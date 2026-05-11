package com.fooddelivery.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RestaurantResponse {
    private Long id;
    private String name;
    private String cuisineType;
    private String address;
    private String status;
    private Double rating;
    private String ownerName;
    private int totalItems;
}