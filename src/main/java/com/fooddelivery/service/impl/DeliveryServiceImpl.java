package com.fooddelivery.service.impl;

import com.fooddelivery.dto.request.DeliveryAgentRegisterRequest;
import com.fooddelivery.dto.response.DeliveryAgentResponse;
import com.fooddelivery.entity.DeliveryAgent;
import com.fooddelivery.entity.Role;
import com.fooddelivery.entity.User;
import com.fooddelivery.enums.OrderStatus;
import com.fooddelivery.enums.RoleType;
import com.fooddelivery.repository.DeliveryAgentRepository;
import com.fooddelivery.repository.OrderRepository;
import com.fooddelivery.repository.RoleRepository;
import com.fooddelivery.repository.UserRepository;
import com.fooddelivery.service.DeliveryService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryAgentRepository deliveryAgentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // ✅ Assign nearest + least busy agent
    @Override
    public DeliveryAgent assignAgent(Double deliveryLatitude, Double deliveryLongitude) {

        List<DeliveryAgent> availableAgents = deliveryAgentRepository.findByAvailableTrue();

        if (availableAgents.isEmpty()) {
            throw new RuntimeException("No delivery agent available");
        }

        List<OrderStatus> activeStatuses = List.of(
                OrderStatus.PLACED,
                OrderStatus.CONFIRMED,
                OrderStatus.IN_KITCHEN,
                OrderStatus.OUT_FOR_DELIVERY
        );

        Comparator<DeliveryAgent> comparator = Comparator
                .comparingDouble((DeliveryAgent a) ->
                        distance(deliveryLatitude, deliveryLongitude, a))
                .thenComparingLong(a ->
                        orderRepository.countByDeliveryAgent_IdAndStatusIn(a.getId(), activeStatuses))
                .thenComparing(DeliveryAgent::getId);

        return availableAgents.stream()
                .sorted(comparator)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No delivery agent available"));
    }

    // ✅ Distance calculation
    private double distance(Double lat, Double lon, DeliveryAgent agent) {

        if (lat == null || lon == null ||
            agent.getLatitude() == null || agent.getLongitude() == null) {
            return Double.MAX_VALUE;
        }

        return Math.hypot(agent.getLatitude() - lat, agent.getLongitude() - lon);
    }

    // ✅ Mark agent busy
    @Override
    public void markBusy(DeliveryAgent agent) {
        agent.setAvailable(false);
        deliveryAgentRepository.save(agent);
    }

    // ✅ Mark agent available
    @Override
    public void markAvailable(DeliveryAgent agent) {
        agent.setAvailable(true);
        deliveryAgentRepository.save(agent);
    }

  @Override
@Transactional
public DeliveryAgentResponse registerDeliveryAgent(DeliveryAgentRegisterRequest request) {

    if (userRepository.findByEmail(request.getEmail()).isPresent()) {
        throw new RuntimeException("Email already registered");
    }

    // 1️⃣ Create User
    User user = new User();
    user.setName(request.getName());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setPhone(request.getPhone());

    Role role = roleRepository.findByName(RoleType.DELIVERY_AGENT)
            .orElseThrow(() -> new RuntimeException("Role not found"));

    user.setRole(role);
    userRepository.save(user);

    // 2️⃣ Create DeliveryAgent
    DeliveryAgent agent = new DeliveryAgent();
    agent.setUser(user);
    agent.setAvailable(true);
    agent.setLatitude(request.getLatitude());
    agent.setLongitude(request.getLongitude());

    deliveryAgentRepository.save(agent);

    // ✅ Return response
    return new DeliveryAgentResponse(
            agent.getId(),
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPhone(),
            agent.isAvailable(),
            agent.getLatitude(),
            agent.getLongitude()
    );
}



@Override
public List<DeliveryAgentResponse> getAllAgents() {

    return deliveryAgentRepository.findAll()
            .stream()
            .map(agent -> new DeliveryAgentResponse(
                    agent.getId(),
                    agent.getUser().getId(),
                    agent.getUser().getName(),
                    agent.getUser().getEmail(),
                    agent.getUser().getPhone(),
                    agent.isAvailable(),
                    agent.getLatitude(),
                    agent.getLongitude()
            ))
            .toList();
}


@Override
public DeliveryAgentResponse updateAgent(Long id, DeliveryAgentRegisterRequest request) {

    DeliveryAgent agent = deliveryAgentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Agent not found"));

    agent.setLatitude(request.getLatitude());
    agent.setLongitude(request.getLongitude());
    agent.setAvailable(true);

    deliveryAgentRepository.save(agent);

    return new DeliveryAgentResponse(
            agent.getId(),
            agent.getUser().getId(),
            agent.getUser().getName(),
            agent.getUser().getEmail(),
            agent.getUser().getPhone(),
            agent.isAvailable(),
            agent.getLatitude(),
            agent.getLongitude()
    );
}

@Override
public void updateAvailability(Long id, boolean available) {

    DeliveryAgent agent = deliveryAgentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Agent not found"));

    agent.setAvailable(available);
    deliveryAgentRepository.save(agent);
}

}