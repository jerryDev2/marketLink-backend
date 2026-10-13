package com.BeninCodeX.MarketLink.config;

import com.BeninCodeX.MarketLink.service.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                // Disable CSRF because we are using JWT
                .csrf(csrf -> csrf.disable())

                // Allow React frontend
                .cors(cors -> {})

                // Do not create HTTP sessions
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // PUBLIC AUTHENTICATION ENDPOINTS
                        .requestMatchers(
                                "/api/user",
                                "/api/login",
                                "/api/test-email",
                                "/api/signup",
                                "/api/verify-signup-code",
                                "/api/forgot-password",
                                "/api/verify-reset-code",
                                "/api/reset-password"
                        ).permitAll()

                        // PUBLIC PRODUCT BROWSING
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/product"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/product/*"
                        ).permitAll()

                        // EVERYTHING ELSE REQUIRES JWT
                        .anyRequest().authenticated()
                )

                // JWT filter
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}