package com.fooddelivery.service.impl;

import com.fooddelivery.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendHtmlEmail(String to, String subject, String html) {

        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true); // ✅ HTML enabled

            mailSender.send(message);

            System.out.println("✅ Email sent via SMTP");

        } catch (Exception e) {
            System.out.println("❌ Email failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}