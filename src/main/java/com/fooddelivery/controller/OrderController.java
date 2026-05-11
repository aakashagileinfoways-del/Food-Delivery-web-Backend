package com.fooddelivery.controller;

import com.fooddelivery.dto.request.OrderRequest;
import com.fooddelivery.dto.response.OrderResponse;
import com.fooddelivery.enums.OrderStatus;
import com.fooddelivery.common.ApiResponse;
import com.fooddelivery.service.OrderService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/place")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ApiResponse<String> placeOrder(
            @RequestBody OrderRequest request,
            Authentication authentication) {
        return new ApiResponse<>(
                true,
                200,
                "Order placed",
                orderService.placeOrder(request, authentication.getName()));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','RESTAURANT_OWNER','DELIVERY_AGENT')")
    public ApiResponse<String> updateStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status,
            Authentication authentication) {
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .orElseThrow(() -> new RuntimeException("Role not found"));
        orderService.updateOrderStatus(id, status, role);
        return new ApiResponse<>(true, 200, "Status updated", null);
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderResponse> getOrder(@PathVariable Long id) {
        return new ApiResponse<>(
                true,
                200,
                "Order details",
                orderService.getOrderDetails(id));
    }

 
@GetMapping("/my-orders")
public ApiResponse<List<OrderResponse>> getMyOrders(Authentication authentication) {

    String email = authentication.getName();

    return new ApiResponse<>(
            true,
            200,
            "Order history fetched",
            orderService.getOrdersByUser(email)
    );
}

}