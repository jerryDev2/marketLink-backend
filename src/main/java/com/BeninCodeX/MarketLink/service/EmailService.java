
package com.BeninCodeX.MarketLink.service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.SendEmailRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final Resend resend;
    private final String fromEmail;

    public EmailService(
            @Value("${RESEND_API_KEY}") String apiKey,
            @Value("${EMAIL_FROM:onboarding@resend.dev}") String fromEmail
    ) {
        this.resend = new Resend(apiKey);
        this.fromEmail = fromEmail;
    }

    public void sendVerificationCode(String email, String code) {
        String message =
                "Hello,\n\n" +
                        "Thank you for signing up for MarketLink.\n\n" +
                        "Your verification code is:\n\n" +
                        code + "\n\n" +
                        "This code will expire in 10 minutes.\n\n" +
                        "If you did not create a MarketLink account, please ignore this email.\n\n" +
                        "Regards,\nMarketLink Team";

        sendEmail(
                email,
                "MarketLink Email Verification",
                message
        );
    }

    public void sendPasswordResetCode(String email, String code) {
        String message =
                "Hello,\n\n" +
                        "We received a request to reset your MarketLink password.\n\n" +
                        "Your password reset code is:\n\n" +
                        code + "\n\n" +
                        "This code will expire in 10 minutes.\n\n" +
                        "If you did not request a password reset, please ignore this email.\n\n" +
                        "Regards,\nMarketLink Team";

        sendEmail(
                email,
                "MarketLink Password Reset",
                message
        );
    }

    private void sendEmail(
            String email,
            String subject,
            String message
    ) {
        SendEmailRequest params = SendEmailRequest.builder()
                .from(fromEmail)
                .to(email)
                .subject(subject)
                .text(message)
                .build();

        try {
            resend.emails().send(params);
        } catch (ResendException e) {
            throw new RuntimeException(
                    "Unable to send email through Resend: " + e.getMessage(),
                    e
            );
        }
    }
}
