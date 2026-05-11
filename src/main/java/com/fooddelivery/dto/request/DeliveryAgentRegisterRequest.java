package com.fooddelivery.dto.request;

import lombok.Data;

@Data
public class DeliveryAgentRegisterRequest {

    private String name;
    private String email;
    private String password;
    private String phone;
    private Double latitude;
    private Double longitude;
};
