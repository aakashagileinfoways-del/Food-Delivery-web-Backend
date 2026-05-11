package com.fooddelivery.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MenuResponse {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Boolean available;
    private String category;
    private Long restaurantId;
}
