package com.fooddelivery.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fooddelivery.entity.NewUser;

public interface NewUserRepository extends JpaRepository<NewUser, Long> {
    List<NewUser> findByStatus(String status);
    Optional<NewUser> findByEmail(String email);
}