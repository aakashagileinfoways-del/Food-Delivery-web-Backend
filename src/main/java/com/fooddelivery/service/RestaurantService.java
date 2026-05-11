package com.fooddelivery.service;

import com.fooddelivery.dto.request.RestaurantRequest;
import com.fooddelivery.dto.response.RestaurantResponse;
import com.fooddelivery.enums.RestaurantStatus;

import java.util.List;

public interface RestaurantService {

    RestaurantResponse createRestaurant(RestaurantRequest request, String ownerEmail);

    List<RestaurantResponse> getAllRestaurants();

    RestaurantResponse updateStatus(Long id, RestaurantStatus status);

    List<RestaurantResponse> searchRestaurants(String cuisine, Double minRating);

}