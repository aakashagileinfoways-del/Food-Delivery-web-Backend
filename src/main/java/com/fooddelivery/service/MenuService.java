package com.fooddelivery.service;

import java.util.List;

import com.fooddelivery.dto.request.MenuRequest;
import com.fooddelivery.dto.response.MenuResponse;

public interface MenuService {

    MenuResponse addItem(Long restaurantId, MenuRequest request);

    List<MenuResponse> getMenuByRestaurant(Long restaurantId);

    List<MenuResponse> searchAvailableDishes(String cuisineType, Double minRating);

    MenuResponse updateItem(Long id, MenuRequest request);

    void deleteItem(Long id);

    void updateAvailability(Long id, Boolean available);
}