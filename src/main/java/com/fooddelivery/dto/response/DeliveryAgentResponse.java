package com.fooddelivery.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeliveryAgentResponse {

    private Long agentId;
    private Long userId;
    private String name;
    private String email;
    private String phone;
    private boolean available;
    private Double latitude;
    private Double longitude;
}