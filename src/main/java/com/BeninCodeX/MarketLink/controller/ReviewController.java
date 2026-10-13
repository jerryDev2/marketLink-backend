package com.BeninCodeX.MarketLink.controller;

import com.BeninCodeX.MarketLink.entity.Review;
import com.BeninCodeX.MarketLink.service.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // CREATE REVIEW
    @PostMapping
    public Review createReview(
            @RequestBody Map<String, Object> request
    ) {

        long userId = Long.parseLong(
                request.get("userId").toString()
        );

        long productId = Long.parseLong(
                request.get("productId").toString()
        );

        int rating = Integer.parseInt(
                request.get("rating").toString()
        );

        String comment =
                request.get("comment").toString();

        return reviewService.createReview(
                userId,
                productId,
                rating,
                comment
        );
    }

    // GET REVIEWS FOR A PRODUCT
    @GetMapping("/product/{productId}")
    public List<Review> getProductReviews(
            @PathVariable long productId
    ) {

        return reviewService.findByProduct(productId);
    }

    // GET REVIEWS WRITTEN BY A USER
    @GetMapping("/user/{userId}")
    public List<Review> getUserReviews(
            @PathVariable long userId
    ) {

        return reviewService.findByUser(userId);
    }

    // GET ONE REVIEW
    @GetMapping("/{reviewId}")
    public Review getReview(
            @PathVariable long reviewId
    ) {

        return reviewService.findById(reviewId);
    }

    // UPDATE REVIEW
    @PutMapping("/{reviewId}")
    public Review updateReview(
            @PathVariable long reviewId,
            @RequestBody Map<String, Object> request
    ) {

        int rating = Integer.parseInt(
                request.get("rating").toString()
        );

        String comment =
                request.get("comment").toString();

        return reviewService.updateReview(
                reviewId,
                rating,
                comment
        );
    }

    // DELETE REVIEW
    @DeleteMapping("/{reviewId}")
    public String deleteReview(
            @PathVariable long reviewId
    ) {

        reviewService.deleteById(reviewId);

        return "Review deleted successfully";
    }
}