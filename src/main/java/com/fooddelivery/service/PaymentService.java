package com.fooddelivery.service;

public interface PaymentService {
    /**
     * Mock payment processing. In a real system this would call a payment gateway.
     *
     * @return a mock transaction id
     */
    String processPayment(String paymentType, Double amount, String customerEmail);
}

