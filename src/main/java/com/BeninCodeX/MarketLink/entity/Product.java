package com.BeninCodeX.MarketLink.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productId")
    private long productId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    // Keep category as String for now.
    // You don't need a Category table or endpoint.
    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "quantity", nullable = false)
    private long quantity;

    // Examples: kg, basket, crate, bunch, bag, piece
    @Column(name = "unit", nullable = false)
    private String unit;

    @Column(name = "imageUrl", nullable = false, length = 1000)
    private String imageUrl;

    // The farmer who owns this product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmerId", nullable = false)
    @JsonIgnore
    private FarmerProfile farmerProfile;


    // Empty constructor required by JPA
    public Product() {
    }


    // Constructor
    public Product(
            String name,
            String description,
            String category,
            double price,
            long quantity,
            String unit,
            String imageUrl,
            FarmerProfile farmerProfile
    ) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.unit = unit;
        this.imageUrl = imageUrl;
        this.farmerProfile = farmerProfile;
    }


    // Getters and Setters

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }


    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public FarmerProfile getFarmerProfile() {
        return farmerProfile;
    }

    public void setFarmerProfile(FarmerProfile farmerProfile) {
        this.farmerProfile = farmerProfile;
    }
}