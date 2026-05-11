package com.fooddelivery.dto.request;

import lombok.Data;

import com.fooddelivery.enums.FoodCategory; 

@Data
public class MenuRequest {
    private String name;
    private String description;
    private Double price;
    private FoodCategory category;
}