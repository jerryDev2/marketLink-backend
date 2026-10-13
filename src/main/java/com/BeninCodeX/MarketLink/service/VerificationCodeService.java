package com.BeninCodeX.MarketLink.service;

import com.BeninCodeX.MarketLink.entity.VerificationCode;
import com.BeninCodeX.MarketLink.repository.VerificationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class VerificationCodeService {

    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;

    @Autowired
    public VerificationCodeService(
            VerificationCodeRepository verificationCodeRepository,
            EmailService emailService) {

        this.verificationCodeRepository = verificationCodeRepository;
        this.emailService = emailService;
    }

    public void sendCode(String email) {

        // Generate a 6-digit code
        String code = String.format(
                "%06d",
                new Random().nextInt(1000000)
        );

        // Check if this email already has a code
        VerificationCode verificationCode =
                verificationCodeRepository.findByEmail(email);

        if (verificationCode == null) {

            verificationCode = new VerificationCode();

            verificationCode.setEmail(email);
        }

        verificationCode.setCode(code);

        // Code expires after 10 minutes
        verificationCode.setExpiresAt(
                LocalDateTime.now().plusMinutes(10)
        );

        verificationCodeRepository.save(verificationCode);

        // Send code to email
        emailService.sendVerificationCode(email, code);
    }

    public boolean verifyCode(String email, String code) {

        VerificationCode verificationCode =
                verificationCodeRepository.findByEmail(email);

        if (verificationCode == null) {
            return false;
        }

        // Check expiration
        if (LocalDateTime.now()
                .isAfter(verificationCode.getExpiresAt())) {

            return false;
        }

        // Check code
        if (!verificationCode.getCode().equals(code)) {
            return false;
        }

        // Code is correct
        verificationCodeRepository.delete(verificationCode);

        return true;
    }
}