package com.BeninCodeX.MarketLink.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationCode(String email, String code) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("MarketLink Email Verification");

        message.setText(
                "Hello,\n\n" +
                        "Thank you for signing up for MarketLink.\n\n" +
                        "Your verification code is:\n\n" +
                        code + "\n\n" +
                        "This code will expire in 10 minutes.\n\n" +
                        "If you did not create a MarketLink account, please ignore this email.\n\n" +
                        "Regards,\n" +
                        "MarketLink Team"
        );

        mailSender.send(message);
    }

    public void sendPasswordResetCode(String email, String code) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("MarketLink Password Reset");

        message.setText(
                "Hello,\n\n" +
                        "We received a request to reset your MarketLink password.\n\n" +
                        "Your password reset code is:\n\n" +
                        code + "\n\n" +
                        "This code will expire in 10 minutes.\n\n" +
                        "If you did not request a password reset, please ignore this email.\n\n" +
                        "Regards,\n" +
                        "MarketLink Team"
        );

        mailSender.send(message);
    }
}