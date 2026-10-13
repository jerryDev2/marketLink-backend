package com.BeninCodeX.MarketLink.controller;

import com.BeninCodeX.MarketLink.dto.*;
import com.BeninCodeX.MarketLink.entity.PendingSignup;
import com.BeninCodeX.MarketLink.entity.User;
import com.BeninCodeX.MarketLink.repository.PendingSignupRepository;
import com.BeninCodeX.MarketLink.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    private UserService userService;
    private JwtService jwtService;
    private PasswordEncoder passwordEncoder;
    private FarmerProfileService farmerProfileService;
    private final CartService cartService;
    private final PendingSignupRepository pendingSignupRepository;
    private final VerificationCodeService verificationCodeService;
    private final PasswordResetService passwordResetService;

    @Autowired
    public UserController(
            UserService userService,
            CartService cartService,
            FarmerProfileService farmerProfileService,
            JwtService jwtService,
            PasswordEncoder passwordEncoder, PendingSignupRepository pendingSignupRepository, VerificationCodeService verificationCodeService, PasswordResetService passwordResetService
    ) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.cartService = cartService;
        this.farmerProfileService = farmerProfileService;
        this.passwordEncoder = passwordEncoder;
        this.pendingSignupRepository = pendingSignupRepository;
        this.verificationCodeService = verificationCodeService;
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/user")
    public Map<String, Object> save(@RequestBody User theUser) {

        User savedUser = userService.save(theUser);

        // Create farmer profile only when the user is a farmer
        if ("FARMER".equalsIgnoreCase(savedUser.getRole())) {
            farmerProfileService.createProfile(savedUser);
        }

        // Create customer cart only when the user is a customer
        if ("CUSTOMER".equalsIgnoreCase(savedUser.getRole())) {
            cartService.createCart(savedUser);
        }

        // Generate JWT
        String token = jwtService.generateToken(savedUser);

        UserResponse userResponse = new UserResponse(
                savedUser.getUserId(),
                savedUser.getRole(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                savedUser.getPhoneNumber()
        );

        Map<String, Object> response = new HashMap<>();

        response.put("user", userResponse);
        response.put("token", token);

        return response;
    }
    @PostMapping("/signup")
    public Map<String, Object> signup(@RequestBody SignupRequest request) {

        // Check whether email is already registered
        User existingUser = userService.findByEmail(request.getEmail());

        if (existingUser != null) {
            throw new RuntimeException("Email is already registered");
        }

        // Check if this email already has a pending signup
        PendingSignup pendingSignup =
                pendingSignupRepository.findByEmail(request.getEmail());

        if (pendingSignup == null) {

            pendingSignup = new PendingSignup();

        }

        pendingSignup.setFirstName(request.getFirstName());
        pendingSignup.setLastName(request.getLastName());
        pendingSignup.setEmail(request.getEmail());
        pendingSignup.setPhoneNumber(request.getPhoneNumber());
        pendingSignup.setPassword(request.getPassword());
        pendingSignup.setRole(request.getRole());

        pendingSignupRepository.save(pendingSignup);

        // Send verification code
        verificationCodeService.sendCode(request.getEmail());

        Map<String, Object> response = new HashMap<>();

        response.put("message", "Verification code sent to your email");
        response.put("email", request.getEmail());

        return response;
    }


    @PostMapping("/verify-signup-code")
    public Map<String, Object> verifySignupCode(
            @RequestBody VerifyCodeRequest request) {

        boolean valid = verificationCodeService.verifyCode(
                request.getEmail(),
                request.getCode()
        );

        if (!valid) {
            throw new RuntimeException("Invalid or expired verification code");
        }

        PendingSignup pendingSignup =
                pendingSignupRepository.findByEmail(request.getEmail());

        if (pendingSignup == null) {
            throw new RuntimeException("Signup information not found");
        }

        // Create the actual User
        User newUser = new User();

        newUser.setFirstName(pendingSignup.getFirstName());
        newUser.setLastName(pendingSignup.getLastName());
        newUser.setEmail(pendingSignup.getEmail());
        newUser.setPhoneNumber(pendingSignup.getPhoneNumber());
        newUser.setPassword(pendingSignup.getPassword());
        newUser.setRole(pendingSignup.getRole());

        // UserService.save() will BCrypt the password
        User savedUser = userService.save(newUser);

        // Create farmer profile
        if ("FARMER".equalsIgnoreCase(savedUser.getRole())) {
            farmerProfileService.createProfile(savedUser);
        }

        // Create customer cart
        if ("CUSTOMER".equalsIgnoreCase(savedUser.getRole())) {
            cartService.createCart(savedUser);
        }

        // Generate JWT
        String token = jwtService.generateToken(savedUser);

        // Delete pending signup
        pendingSignupRepository.delete(pendingSignup);

        UserResponse userResponse = new UserResponse(
                savedUser.getUserId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                savedUser.getRole(),
                savedUser.getPhoneNumber()
        );

        Map<String, Object> response = new HashMap<>();

        response.put("user", userResponse);
        response.put("token", token);
        response.put("message", "Email verified and account created successfully");

        return response;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody User theUser) {

        User saveUser = userService.findByEmail(theUser.getEmail());

        if (saveUser == null) {
            throw new RuntimeException("invalid email or password");
        }

        String storedPassword = saveUser.getPassword();

        boolean passwordMatches =
                passwordEncoder.matches(
                        theUser.getPassword(),
                        storedPassword
                );

        if (!passwordMatches) {
            throw new RuntimeException("invalid email or password");
        }

        String token = jwtService.generateToken(saveUser);

        UserResponse userResponse = new UserResponse(
                saveUser.getUserId(),
                saveUser.getFirstName(),
                saveUser.getLastName(),
                saveUser.getEmail(),
                saveUser.getRole(),
                saveUser.getPhoneNumber()
        );

        Map<String, Object> response = new HashMap<>();

        response.put("user", userResponse);
        response.put("token", token);

        return response;
    }
    @PostMapping("/forgot-password")
    public Map<String, Object> forgotPassword(
            @RequestBody ForgotPasswordRequest request
    ) {

        passwordResetService.sendResetCode(
                request.getEmail()
        );

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "message",
                "If an account exists for that email, a password reset code has been sent."
        );

        return response;
    }
    @PostMapping("/reset-password")
    public Map<String, Object> resetPassword(
            @RequestBody ResetPasswordRequest request
    ) {

        if (request.getNewPassword() == null ||
                request.getNewPassword().length() < 6) {

            throw new RuntimeException(
                    "Password must be at least 6 characters"
            );
        }

        passwordResetService.resetPassword(
                request.getEmail(),
                request.getCode(),
                request.getNewPassword()
        );

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "message",
                "Password reset successfully"
        );

        return response;
    }
}