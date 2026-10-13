package com.BeninCodeX.MarketLink.service;

import com.BeninCodeX.MarketLink.entity.FarmerProfile;
import com.BeninCodeX.MarketLink.entity.User;
import com.BeninCodeX.MarketLink.repository.FarmerProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FarmerProfileService {

    private final FarmerProfileRepository farmerProfileRepository;

    public FarmerProfileService(FarmerProfileRepository farmerProfileRepository) {
        this.farmerProfileRepository = farmerProfileRepository;
    }

    public FarmerProfile createProfile(User user) {

        FarmerProfile profile = new FarmerProfile();

        profile.setUser(user);
        profile.setFarmName("My Farm");
        profile.setDescription("");
        profile.setLocation("");
        profile.setProfileImage("");

        return farmerProfileRepository.save(profile);
    }

    public Optional<FarmerProfile> findByUser(User user) {
        return farmerProfileRepository.findByUser(user);
    }

    public FarmerProfile updateProfile(FarmerProfile profile) {
        return farmerProfileRepository.save(profile);
    }
}