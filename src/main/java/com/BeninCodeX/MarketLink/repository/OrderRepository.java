package com.BeninCodeX.MarketLink.repository;

import com.BeninCodeX.MarketLink.entity.Order;
import com.BeninCodeX.MarketLink.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);

    @Query("""
        SELECT DISTINCT o
        FROM Order o
        JOIN o.items oi
        JOIN oi.product p
        WHERE p.farmerProfile.farmerId = :farmerId
    """)
    List<Order> findOrdersByFarmerId(@Param("farmerId") long farmerId);
}