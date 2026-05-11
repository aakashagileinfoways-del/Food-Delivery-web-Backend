package com.fooddelivery.service.impl;

import com.fooddelivery.service.PaymentService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Override
    public String processPayment(String paymentType, Double amount, String customerEmail) {
        if (paymentType == null || paymentType.isBlank()) {
            throw new RuntimeException("Payment type is required");
        }
        if (amount == null || amount <= 0) {
            throw new RuntimeException("Invalid order amount");
        }
        if (customerEmail == null || customerEmail.isBlank()) {
            throw new RuntimeException("Customer email is required for payment");
        }
        // Mock success response
        return "TXN-" + UUID.randomUUID();
    }
}

