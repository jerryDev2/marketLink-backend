package com.BeninCodeX.MarketLink.controller;

import com.BeninCodeX.MarketLink.entity.FarmerProfile;
import com.BeninCodeX.MarketLink.entity.Product;
import com.BeninCodeX.MarketLink.entity.User;
import com.BeninCodeX.MarketLink.repository.FarmerProfileRepository;
import com.BeninCodeX.MarketLink.repository.UserRepository;
import com.BeninCodeX.MarketLink.service.CloudinaryService;
import com.BeninCodeX.MarketLink.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;
    private final UserRepository userRepository;
    private final FarmerProfileRepository farmerProfileRepository;
    private final CloudinaryService cloudinaryService;


    @Autowired
    public ProductController(
            ProductService productService,
            UserRepository userRepository,
            FarmerProfileRepository farmerProfileRepository,
            CloudinaryService cloudinaryService
    ) {
        this.productService = productService;
        this.userRepository = userRepository;
        this.farmerProfileRepository = farmerProfileRepository;
        this.cloudinaryService = cloudinaryService;
    }


    // CREATE PRODUCT + IMAGE
    @PostMapping(
            value = "/product",
            consumes = "multipart/form-data"
    )
    public Product addProduct(

            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String category,
            @RequestParam double price,
            @RequestParam long quantity,
            @RequestParam String unit,
            @RequestParam MultipartFile image,

            Authentication authentication
    ) throws IOException {

        // 1. Get logged-in user's email from JWT
        String email = authentication.getName();


        // 2. Find User
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }


        // 3. Find farmer profile
        FarmerProfile farmerProfile =
                farmerProfileRepository.findByUser(user)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Farmer profile not found"
                                )
                        );


        // 4. Upload image to Cloudinary
        String imageUrl =
                cloudinaryService.uploadImage(image);


        // 5. Create product
        Product product = new Product();

        product.setName(name);
        product.setDescription(description);
        product.setCategory(category);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setUnit(unit);

        // Save Cloudinary URL
        product.setImageUrl(imageUrl);

        // Connect product to farmer
        product.setFarmerProfile(farmerProfile);


        // 6. Save product
        return productService.save(product);
    }


    // GET ALL PRODUCTS
    @GetMapping("/product")
    public List<Product> getProducts() {

        return productService.findAll();
    }


    // GET ONE PRODUCT
    @GetMapping("/product/{productId}")
    public Product findProductById(
            @PathVariable long productId
    ) {

        return productService.findById(productId);
    }


    // GET PRODUCTS BELONGING TO LOGGED-IN FARMER
    @GetMapping("/product/my-products")
    public List<Product> getMyProducts(
            Authentication authentication
    ) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        FarmerProfile farmerProfile =
                farmerProfileRepository.findByUser(user)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Farmer profile not found"
                                )
                        );

        return productService.findByFarmerId(
                farmerProfile.getFarmerId()
        );
    }


    // UPDATE PRODUCT
    @PutMapping("/product/{productId}")
    public Product updateProduct(
            @PathVariable long productId,
            @RequestBody Product theProduct,
            Authentication authentication
    ) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        FarmerProfile farmerProfile =
                farmerProfileRepository.findByUser(user)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Farmer profile not found"
                                )
                        );

        Product existingProduct =
                productService.findById(productId);


        // Make sure product belongs to this farmer
        if (existingProduct.getFarmerProfile().getFarmerId()
                != farmerProfile.getFarmerId()) {

            throw new RuntimeException(
                    "You are not allowed to update this product"
            );
        }


        existingProduct.setName(theProduct.getName());
        existingProduct.setDescription(theProduct.getDescription());
        existingProduct.setCategory(theProduct.getCategory());
        existingProduct.setPrice(theProduct.getPrice());
        existingProduct.setQuantity(theProduct.getQuantity());
        existingProduct.setUnit(theProduct.getUnit());

        // Keep existing image for now
        // We will handle changing images separately.

        return productService.save(existingProduct);
    }


    // DELETE PRODUCT
    @DeleteMapping("/product/{productId}")
    public String deleteProduct(
            @PathVariable long productId,
            Authentication authentication
    ) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        FarmerProfile farmerProfile =
                farmerProfileRepository.findByUser(user)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Farmer profile not found"
                                )
                        );


        Product product =
                productService.findById(productId);


        if (product.getFarmerProfile().getFarmerId()
                != farmerProfile.getFarmerId()) {

            throw new RuntimeException(
                    "You are not allowed to delete this product"
            );
        }


        productService.deleteById(productId);

        return "Product deleted successfully";
    }
}