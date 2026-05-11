package com.fooddelivery.controller;

import com.fooddelivery.common.ApiResponse;
import com.fooddelivery.dto.request.RestaurantRequest;
import com.fooddelivery.dto.response.RestaurantResponse;
import com.fooddelivery.enums.RestaurantStatus;
import com.fooddelivery.service.RestaurantService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    // OWNER only
@PreAuthorize("hasRole('RESTAURANT_OWNER')")
@PostMapping("/create")
    public ApiResponse<RestaurantResponse> createRestaurant(
            @RequestBody RestaurantRequest request,
            Authentication authentication
    ) {

        String email = authentication.getName();

        return new ApiResponse<>(
                true,
                200,
                "Restaurant created successfully",
                restaurantService.createRestaurant(request, email)
        );
    }

    // CUSTOMER
    @GetMapping
    public ApiResponse<List<RestaurantResponse>> getAll() {

        return new ApiResponse<>(
                true,
                200,
                "Restaurants fetched successfully",
                restaurantService.getAllRestaurants()
        );
    }

@PreAuthorize("hasRole('ADMIN')")
@PutMapping("/{id}/status")
public ApiResponse<RestaurantResponse> updateStatus(
        @PathVariable Long id,
        @RequestParam RestaurantStatus status
) {
    return new ApiResponse<>(
            true,
            200,
            "Status updated",
            restaurantService.updateStatus(id, status)
    );
}



@GetMapping("/search")
public ApiResponse<List<RestaurantResponse>> searchRestaurants(
        @RequestParam(required = false) String cuisine,
        @RequestParam(required = false) Double minRating
) {

    return new ApiResponse<>(
            true,
            200,
            "Search results",
            restaurantService.searchRestaurants(cuisine, minRating)
    );
}



}