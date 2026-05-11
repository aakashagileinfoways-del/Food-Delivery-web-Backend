package com.fooddelivery.repository;

import com.fooddelivery.entity.User;
import com.fooddelivery.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    boolean existsByRole_Name(RoleType roleType);

}