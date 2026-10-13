package com.BeninCodeX.MarketLink.repository;

import com.BeninCodeX.MarketLink.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder_OrderId(long orderId);
}