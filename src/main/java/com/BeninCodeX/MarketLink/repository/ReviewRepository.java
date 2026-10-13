package com.BeninCodeX.MarketLink.repository;

import com.BeninCodeX.MarketLink.entity.Product;
import com.BeninCodeX.MarketLink.entity.Review;
import com.BeninCodeX.MarketLink.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProduct(Product product);

    List<Review> findByUser(User user);
}