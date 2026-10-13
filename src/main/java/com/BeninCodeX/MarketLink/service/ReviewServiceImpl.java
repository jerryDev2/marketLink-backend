package com.BeninCodeX.MarketLink.service;

import com.BeninCodeX.MarketLink.entity.Product;
import com.BeninCodeX.MarketLink.entity.Review;
import com.BeninCodeX.MarketLink.entity.User;
import com.BeninCodeX.MarketLink.repository.ProductRepository;
import com.BeninCodeX.MarketLink.repository.ReviewRepository;
import com.BeninCodeX.MarketLink.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ReviewServiceImpl(
            ReviewRepository reviewRepository,
            UserRepository userRepository,
            ProductRepository productRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Review createReview(
            long userId,
            long productId,
            int rating,
            String comment
    ) {

        if (rating < 1 || rating > 5) {
            throw new RuntimeException(
                    "Rating must be between 1 and 5"
            );
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new RuntimeException("Product not found")
                );

        Review review = new Review();

        review.setUser(user);
        review.setProduct(product);
        review.setRating(rating);
        review.setComment(comment);

        return reviewRepository.save(review);
    }

    @Override
    public List<Review> findByProduct(long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new RuntimeException("Product not found")
                );

        return reviewRepository.findByProduct(product);
    }

    @Override
    public List<Review> findByUser(long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        return reviewRepository.findByUser(user);
    }

    @Override
    public Review findById(long reviewId) {

        return reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Review not found"
                        )
                );
    }

    @Override
    public Review updateReview(
            long reviewId,
            int rating,
            String comment
    ) {

        if (rating < 1 || rating > 5) {
            throw new RuntimeException(
                    "Rating must be between 1 and 5"
            );
        }

        Review review = findById(reviewId);

        review.setRating(rating);
        review.setComment(comment);

        return reviewRepository.save(review);
    }

    @Override
    public void deleteById(long reviewId) {

        if (!reviewRepository.existsById(reviewId)) {
            throw new RuntimeException(
                    "Review not found"
            );
        }

        reviewRepository.deleteById(reviewId);
    }
}