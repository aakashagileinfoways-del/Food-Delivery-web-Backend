package com.fooddelivery.kafka.producer;

import com.fooddelivery.kafka.event.OrderEvent;
import com.fooddelivery.kafka.event.PaymentEvent;
import com.fooddelivery.kafka.event.UserEvent;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String ORDER_TOPIC = "order-events";
    private static final String PAYMENT_TOPIC = "payment-events";
    private static final String USER_TOPIC = "user-events";

    public void publishOrder(OrderEvent event) {
        kafkaTemplate.send(ORDER_TOPIC, event);
    }

    public void publishPayment(PaymentEvent event) {
        kafkaTemplate.send(PAYMENT_TOPIC, event);
    }


    public void publishUser(UserEvent event) {
        kafkaTemplate.send(USER_TOPIC, event);
    }
}