package com.fooddelivery.service.impl;

import com.fooddelivery.common.MessageConstants;
import com.fooddelivery.config.JwtUtil;
import com.fooddelivery.dto.request.LoginRequest;
import com.fooddelivery.dto.request.RegisterRequest;
import com.fooddelivery.dto.response.AuthResponse;
import com.fooddelivery.dto.response.UserResponse;
import com.fooddelivery.entity.Role;
import com.fooddelivery.entity.User;
import com.fooddelivery.enums.RoleType;
import com.fooddelivery.repository.RoleRepository;
import com.fooddelivery.repository.UserRepository;
import com.fooddelivery.service.AuthService;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            RoleRepository roleRepository,
            JwtUtil jwtUtil) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtUtil = jwtUtil;
    }


    public List<UserResponse> getAllUsers() {

    return userRepository.findAll()
            .stream()
            .map(user -> new UserResponse(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getRole().getName().name()
            ))
            .toList();
}

    @Override
    public String register(RegisterRequest request) {

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);

        userRepository.save(user);

        return "User Registered Successfully";
    }

    @Override
    public String createAdmin(RegisterRequest request) {

        boolean adminExists = userRepository.existsByRole_Name(RoleType.ADMIN);

        if (adminExists) {
            throw new RuntimeException("Admin already exists");
        }

        Role role = roleRepository.findByName(RoleType.ADMIN)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);

        userRepository.save(user);

        return "Admin created successfully";
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        System.out.println("Login attempt with: " + request.getIdentifier());

        String identifier = request.getIdentifier();

        User user;

        if (identifier.contains("@")) {
            user = userRepository.findByEmail(identifier)
                    .orElseThrow(() -> new RuntimeException("User not found with email"));
        } else {
            user = userRepository.findByPhone(identifier)
                    .orElseThrow(() -> new RuntimeException("User not found with phone"));
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String role = user.getRole().getName().name();
        String name = user.getName();
        String email = user.getEmail();
        String phone = user.getPhone();

        String token = jwtUtil.generateToken(user.getEmail(), role);
        String message = MessageConstants.LOGIN_SUCCESSFUL;

        return new AuthResponse(token, message, role, name, email, phone);
    }
}