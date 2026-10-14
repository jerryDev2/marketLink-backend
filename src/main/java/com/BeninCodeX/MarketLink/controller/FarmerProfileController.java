package com.BeninCodeX.MarketLink.controller;

import com.BeninCodeX.MarketLink.dto.FarmerProfileResponse;
import com.BeninCodeX.MarketLink.entity.FarmerProfile;
import com.BeninCodeX.MarketLink.entity.User;
import com.BeninCodeX.MarketLink.service.CloudinaryService;
import com.BeninCodeX.MarketLink.service.FarmerProfileService;
import com.BeninCodeX.MarketLink.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/farmer-profile")
public class FarmerProfileController {

    private final FarmerProfileService farmerProfileService;
    private final UserService userService;
    private final CloudinaryService cloudinaryService;

    public FarmerProfileController(
            FarmerProfileService farmerProfileService,
            UserService userService,
            CloudinaryService cloudinaryService
    ) {
        this.farmerProfileService = farmerProfileService;
        this.userService = userService;
        this.cloudinaryService = cloudinaryService;
    }

    // ==========================================
    // GET MY FARMER PROFILE
    // ==========================================

    @GetMapping("/me")
    public ResponseEntity<FarmerProfileResponse> getMyFarmerProfile(
            Authentication authentication
    ) {

        String email = authentication.getName();

        User user = userService.findByEmail(email);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return farmerProfileService.findByUser(user)
                .map(profile -> ResponseEntity.ok(
                        buildResponse(user, profile)
                ))
                .orElse(ResponseEntity.notFound().build());
    }

    // ==========================================
    // CREATE FARMER PROFILE
    // ==========================================

    @PostMapping("/me")
    public ResponseEntity<FarmerProfileResponse> createMyFarmerProfile(
            Authentication authentication
    ) {

        String email = authentication.getName();

        User user = userService.findByEmail(email);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if (farmerProfileService.findByUser(user).isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        FarmerProfile profile =
                farmerProfileService.createProfile(user);

        return ResponseEntity.ok(
                buildResponse(user, profile)
        );
    }

    // ==========================================
    // UPDATE FARMER PROFILE
    // ==========================================

    @PutMapping(value = "/me", consumes = "multipart/form-data")
    public ResponseEntity<FarmerProfileResponse> updateMyFarmerProfile(

            Authentication authentication,

            @RequestParam(required = false)
            String farmName,

            @RequestParam(required = false)
            String location,

            @RequestParam(required = false)
            String description,

            @RequestParam(required = false)
            String phoneNumber,

            @RequestParam(required = false)
            MultipartFile profileImage

    ) {

        String email = authentication.getName();

        User user = userService.findByEmail(email);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return farmerProfileService.findByUser(user)
                .map(profile -> {

                    profile.setFarmName(farmName);
                    profile.setLocation(location);
                    profile.setDescription(description);

                    /*
                     * Update phone number in USER table.
                     */
                    if (phoneNumber != null && !phoneNumber.isBlank()) {
                        user.setPhoneNumber(phoneNumber);
                        userService.save(user);
                    }

                    /*
                     * Upload profile image to Cloudinary.
                     */
                    if (profileImage != null && !profileImage.isEmpty()) {

                        try {

                            String cloudinaryUrl =
                                    cloudinaryService.uploadImage(profileImage);

                            profile.setProfileImage(cloudinaryUrl);

                        } catch (Exception e) {

                            throw new RuntimeException(
                                    "Failed to upload profile image",
                                    e
                            );
                        }
                    }

                    FarmerProfile updatedProfile =
                            farmerProfileService.updateProfile(profile);

                    return ResponseEntity.ok(
                            buildResponse(user, updatedProfile)
                    );
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ==========================================
    // BUILD RESPONSE
    // ==========================================

    private FarmerProfileResponse buildResponse(
            User user,
            FarmerProfile profile
    ) {

        return new FarmerProfileResponse(

                user.getUserId(),

                profile.getFarmerId(),

                user.getFirstName(),

                user.getLastName(),

                user.getEmail(),

                user.getPhoneNumber(),

                profile.getFarmName(),

                profile.getLocation(),

                profile.getDescription(),

                profile.getProfileImage()



        );
    }
}