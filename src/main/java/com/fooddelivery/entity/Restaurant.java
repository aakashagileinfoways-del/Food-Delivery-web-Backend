package com.fooddelivery.entity;

import java.util.List;

import com.fooddelivery.enums.RestaurantStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String cuisineType;

    private String address;

    private boolean active = true;

    @Enumerated(EnumType.STRING)
private RestaurantStatus status; // PENDING, APPROVED, REJECTED

    private Double rating = 0.0;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MenuItem> menuItems;
}