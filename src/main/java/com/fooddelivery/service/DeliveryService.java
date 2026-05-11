package com.fooddelivery.service;

import java.util.List;

import com.fooddelivery.dto.request.DeliveryAgentRegisterRequest;
import com.fooddelivery.dto.response.DeliveryAgentResponse;
import com.fooddelivery.entity.DeliveryAgent;

public interface DeliveryService {

    DeliveryAgent assignAgent(Double deliveryLatitude, Double deliveryLongitude);

    void markBusy(DeliveryAgent agent);

    void markAvailable(DeliveryAgent agent);

DeliveryAgentResponse registerDeliveryAgent(DeliveryAgentRegisterRequest request);
List<DeliveryAgentResponse> getAllAgents();
DeliveryAgentResponse updateAgent(Long id, DeliveryAgentRegisterRequest request);
void updateAvailability(Long id, boolean available);
}