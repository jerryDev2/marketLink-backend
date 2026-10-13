package com.BeninCodeX.MarketLink.repository;

import com.BeninCodeX.MarketLink.entity.FarmerProfile;
import com.BeninCodeX.MarketLink.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FarmerProfileRepository extends JpaRepository<FarmerProfile,Long> {
    Optional<FarmerProfile> findByUser(User user);
}
