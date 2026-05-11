package com.fooddelivery.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;
    private Double price;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private MenuItem menu;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}