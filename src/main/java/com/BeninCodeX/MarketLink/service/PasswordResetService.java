package com.BeninCodeX.MarketLink.service;

import com.BeninCodeX.MarketLink.entity.PasswordResetCode;
import com.BeninCodeX.MarketLink.entity.User;
import com.BeninCodeX.MarketLink.repository.PasswordResetCodeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class PasswordResetService {

    private final PasswordResetCodeRepository resetCodeRepository;
    private final UserService userService;
    private final EmailService emailService;

    public PasswordResetService(
            PasswordResetCodeRepository resetCodeRepository,
            UserService userService,
            EmailService emailService
    ) {
        this.resetCodeRepository = resetCodeRepository;
        this.userService = userService;
        this.emailService = emailService;
    }

    public void sendResetCode(String email) {

        User user = userService.findByEmail(email);

        if (user == null) {
            // Don't reveal whether an account exists.
            return;
        }

        String code = String.format(
                "%06d",
                new Random().nextInt(1_000_000)
        );

        PasswordResetCode resetCode =
                new PasswordResetCode();

        resetCode.setEmail(email);
        resetCode.setCode(code);
        resetCode.setExpiresAt(
                LocalDateTime.now().plusMinutes(10)
        );

        resetCodeRepository.save(resetCode);

        emailService.sendPasswordResetCode(
                email,
                code
        );
    }

    public boolean verifyCode(
            String email,
            String code
    ) {

        PasswordResetCode resetCode =
                resetCodeRepository
                        .findTopByEmailOrderByIdDesc(email);

        if (resetCode == null) {
            return false;
        }

        if (resetCode.getExpiresAt()
                .isBefore(LocalDateTime.now())) {
            return false;
        }

        return resetCode.getCode().equals(code);
    }

    @Transactional
    public void resetPassword(
            String email,
            String code,
            String newPassword
    ) {

        boolean valid =
                verifyCode(email, code);

        if (!valid) {
            throw new RuntimeException(
                    "Invalid or expired reset code"
            );
        }

        User user =
                userService.findByEmail(email);

        if (user == null) {
            throw new RuntimeException(
                    "User not found"
            );
        }

        userService.updatePassword(
                user,
                newPassword
        );

        PasswordResetCode resetCode =
                resetCodeRepository
                        .findTopByEmailOrderByIdDesc(email);

        if (resetCode != null) {
            resetCodeRepository.delete(resetCode);
        }
    }
}