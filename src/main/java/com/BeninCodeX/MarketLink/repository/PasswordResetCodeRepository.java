package com.BeninCodeX.MarketLink.repository;

import com.BeninCodeX.MarketLink.entity.PasswordResetCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetCodeRepository
        extends JpaRepository<PasswordResetCode, Long> {

    PasswordResetCode findTopByEmailOrderByIdDesc(String email);
}