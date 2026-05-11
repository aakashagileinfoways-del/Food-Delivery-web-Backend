package com.fooddelivery.kafka.event;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {

    private Long orderId;
    private String status;
    private String message;
    private LocalDateTime timestamp;
    private String customerEmail;
}