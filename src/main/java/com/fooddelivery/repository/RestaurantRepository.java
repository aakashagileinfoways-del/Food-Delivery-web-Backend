package com.fooddelivery.repository;

import com.fooddelivery.entity.Restaurant;
import com.fooddelivery.enums.RestaurantStatus;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

List<Restaurant> findByCuisineTypeAndRatingGreaterThanEqual(String cuisineType, Double rating);

    Long countByStatus(RestaurantStatus status);

}