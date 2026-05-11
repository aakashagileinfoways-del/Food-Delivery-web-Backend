package com.fooddelivery.entity;

import com.fooddelivery.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deliveryAddress;
    private Double deliveryLatitude;
    private Double deliveryLongitude;
    private String paymentType;
    private String instructions;
    private Double totalAmount;
    // private String transactionId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime orderTime;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    @ManyToOne
@JoinColumn(name = "user_id")
private User user;

@ManyToOne
@JoinColumn(name = "delivery_agent_id")
private DeliveryAgent deliveryAgent;

@Column(name = "transaction_id")
private String transactionId;

@Column(name = "confirmed_time")
private LocalDateTime confirmedTime;

@Column(name = "kitchen_start_time")
private LocalDateTime kitchenStartTime;

@Column(name = "out_for_delivery_time")
private LocalDateTime outForDeliveryTime;

@Column(name = "delivered_time")
private LocalDateTime deliveredTime;
}