package com.fooddelivery.controller;

import com.fooddelivery.common.ApiResponse;
import com.fooddelivery.dto.request.DeliveryAgentRegisterRequest;
import com.fooddelivery.dto.response.DeliveryAgentResponse;
import com.fooddelivery.service.DeliveryService;
import com.fooddelivery.service.OrderService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final OrderService orderService;
        private final DeliveryService deliveryService; // ✅ injected


    // 🚚 Mark order delivered
    @PutMapping("/{orderId}/deliver")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN','DELIVERY_AGENT')")
    public ApiResponse<String> deliverOrder(@PathVariable Long orderId) {

        orderService.deliverOrder(orderId);

        return new ApiResponse<>(
                true,
                200,
                "Order delivered successfully",
                null
        );
    }
@PostMapping("/delivery-agent/register")
public ApiResponse<DeliveryAgentResponse> register(
        @RequestBody DeliveryAgentRegisterRequest request) {

    DeliveryAgentResponse response = deliveryService.registerDeliveryAgent(request);

    return new ApiResponse<>(
            true,
            200,
            "Delivery agent registered successfully",
            response
    );
}


@GetMapping("/agents")
public ApiResponse<List<DeliveryAgentResponse>> getAllAgents() {

    return new ApiResponse<>(
            true,
            200,
            "Agents fetched",
            deliveryService.getAllAgents()
    );
}
@PutMapping("/agent/{id}")
public ApiResponse<DeliveryAgentResponse> updateAgent(
        @PathVariable Long id,
        @RequestBody DeliveryAgentRegisterRequest request) {

    return new ApiResponse<>(
            true,
            200,
            "Agent updated",
            deliveryService.updateAgent(id, request)
    );
}

@PutMapping("/agent/{id}/status")
public ApiResponse<String> updateAvailability(
        @PathVariable Long id,
        @RequestParam boolean available) {

    deliveryService.updateAvailability(id, available);

    return new ApiResponse<>(
            true,
            200,
            "Availability updated",
            null
    );
}
}