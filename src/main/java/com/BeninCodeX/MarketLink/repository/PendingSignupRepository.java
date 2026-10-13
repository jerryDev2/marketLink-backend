package com.BeninCodeX.MarketLink.repository;

import com.BeninCodeX.MarketLink.entity.PendingSignup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PendingSignupRepository
        extends JpaRepository<PendingSignup, Long> {

    PendingSignup findByEmail(String email);

    void deleteByEmail(String email);
}