package com.fooddelivery.kafka.consumer;

import com.fooddelivery.kafka.event.OrderEvent;
import com.fooddelivery.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailConsumer {

    private final EmailService emailService;

    @KafkaListener(topics = "order-events", groupId = "email-group")
    public void consume(OrderEvent event) {

        try {
            String subject = "Order Update #" + event.getOrderId();

            String html = "<h3>" + event.getMessage() + "</h3>"
                    + "<p>Status: " + event.getStatus() + "</p>";

            emailService.sendHtmlEmail(
                    event.getCustomerEmail(),
                    subject,
                    html
            );

            System.out.println("📧 Email sent for order " + event.getOrderId());

        } catch (Exception e) {
            System.out.println("❌ Email failed: " + e.getMessage());
        }
    }
}