package com.fooddelivery.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fooddelivery.common.ApiResponse;
import com.fooddelivery.dto.request.MenuRequest;
import com.fooddelivery.dto.response.MenuResponse;
import com.fooddelivery.service.MenuService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    @PostMapping("/{restaurantId}")
    public ApiResponse<MenuResponse> addItem(
            @PathVariable Long restaurantId,
            @RequestBody MenuRequest request
    ) {
        return new ApiResponse<>(
                true,
                200,
                "Item added",
                menuService.addItem(restaurantId, request)
        );
    }

    @GetMapping("/{restaurantId}")
    public ApiResponse<List<MenuResponse>> getMenu(@PathVariable Long restaurantId) {
        return new ApiResponse<>(
                true,
                200,
                "Menu fetched",
                menuService.getMenuByRestaurant(restaurantId)
        );
    }

    // CUSTOMER: search available dishes across approved restaurants
    @GetMapping("/search")
    public ApiResponse<List<MenuResponse>> searchDishes(
            @RequestParam(required = false) String cuisine,
            @RequestParam(required = false) Double minRating
    ) {
        return new ApiResponse<>(
                true,
                200,
                "Dish search results",
                menuService.searchAvailableDishes(cuisine, minRating)
        );
    }

    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    @PutMapping("/{id}")
    public ApiResponse<MenuResponse> updateItem(
            @PathVariable Long id,
            @RequestBody MenuRequest request
    ) {
        return new ApiResponse<>(
                true,
                200,
                "Item updated",
                menuService.updateItem(id, request)
        );
    }

    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteItem(@PathVariable Long id) {
        menuService.deleteItem(id);

        return new ApiResponse<>(
                true,
                200,
                "Item deleted",
                "SUCCESS"
        );
    }



    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
@PutMapping("/{id}/availability")
public ApiResponse<String> updateAvailability(
        @PathVariable Long id,
        @RequestParam Boolean available
) {
    menuService.updateAvailability(id, available);

    return new ApiResponse<>(true, 200, "Availability updated", null);
}
}