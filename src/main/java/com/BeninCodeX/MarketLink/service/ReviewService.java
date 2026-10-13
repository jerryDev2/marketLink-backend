package com.BeninCodeX.MarketLink.service;

import com.BeninCodeX.MarketLink.entity.Review;

import java.util.List;

public interface ReviewService {

    Review createReview(
            long userId,
            long productId,
            int rating,
            String comment
    );

    List<Review> findByProduct(long productId);

    List<Review> findByUser(long userId);

    Review findById(long reviewId);

    Review updateReview(
            long reviewId,
            int rating,
            String comment
    );

    void deleteById(long reviewId);
}