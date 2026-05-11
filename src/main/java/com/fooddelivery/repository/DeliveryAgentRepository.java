package com.fooddelivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fooddelivery.entity.DeliveryAgent;

import java.util.List;
import java.util.Optional;

public interface DeliveryAgentRepository extends JpaRepository<DeliveryAgent, Long> {

    Optional<DeliveryAgent> findFirstByAvailableTrue();

    List<DeliveryAgent> findByAvailableTrue();
}