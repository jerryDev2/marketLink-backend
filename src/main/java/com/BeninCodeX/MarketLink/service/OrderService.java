package com.BeninCodeX.MarketLink.service;

import com.BeninCodeX.MarketLink.entity.Order;
import com.BeninCodeX.MarketLink.entity.User;

import java.util.List;

public interface OrderService {

    Order createOrder(long userId, String pickupPlace);

    List<Order> findAll();

    List<Order> findByUser(User user);

    Order findById(long orderId);

    Order updateOrderStatus(long orderId, String status);
    List<Order> findOrdersByFarmerId(long farmerId);

    void deleteById(long orderId);
}