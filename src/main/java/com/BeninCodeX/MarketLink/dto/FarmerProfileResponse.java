package com.BeninCodeX.MarketLink.dto;

public class FarmerProfileResponse {

    private Long userId;

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    private String farmName;
    private String location;
    private String description;
    private String profileImage;
    private Long farmerId;

    public FarmerProfileResponse() {
    }


    public FarmerProfileResponse(
            Long userId,
            Long farmerId,
            String firstName,
            String lastName,
            String email,
            String phoneNumber,
            String farmName,
            String location,
            String description,
            String profileImage
    ) {
        this.userId = userId;
        this.farmerId = farmerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.farmName = farmName;
        this.location = location;
        this.description = description;
        this.profileImage = profileImage;
    }



    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(Long farmerId) {
        this.farmerId = farmerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}