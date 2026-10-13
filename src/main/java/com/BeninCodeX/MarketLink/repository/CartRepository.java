package com.BeninCodeX.MarketLink.repository;

import com.BeninCodeX.MarketLink.entity.Cart;
import com.BeninCodeX.MarketLink.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
