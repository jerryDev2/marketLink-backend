package com.BeninCodeX.MarketLink.repository;

import com.BeninCodeX.MarketLink.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationCodeRepository
        extends JpaRepository<VerificationCode, Long> {

    VerificationCode findByEmail(String email);
}