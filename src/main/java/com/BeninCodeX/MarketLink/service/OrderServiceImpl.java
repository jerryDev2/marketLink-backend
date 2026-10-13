package com.BeninCodeX.MarketLink.service;

import com.BeninCodeX.MarketLink.entity.Cart;
import com.BeninCodeX.MarketLink.entity.CartItem;
import com.BeninCodeX.MarketLink.entity.Order;
import com.BeninCodeX.MarketLink.entity.OrderItem;
import com.BeninCodeX.MarketLink.entity.User;
import com.BeninCodeX.MarketLink.repository.CartRepository;
import com.BeninCodeX.MarketLink.repository.OrderRepository;
import com.BeninCodeX.MarketLink.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            CartRepository cartRepository,
            UserRepository userRepository
    ) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }


    @Override
    @Transactional
    public Order createOrder(long userId, String pickupPlace) {

        // Find the customer
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );


        // Find customer's cart
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found")
                );


        // Make sure cart is not empty
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cannot create order from an empty cart");
        }


        // Create the order
        Order order = new Order();

        order.setUser(user);
        order.setPickupPlace(pickupPlace);
        order.setStatus("PENDING");


        double totalAmount = 0;


        // Convert every CartItem into an OrderItem
        for (CartItem cartItem : cart.getItems()) {

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());

            // Save the product price at the time of ordering
            orderItem.setPrice(cartItem.getProduct().getPrice());

            order.getItems().add(orderItem);


            totalAmount +=
                    cartItem.getProduct().getPrice()
                            * cartItem.getQuantity();
        }


        // Save total
        order.setTotalAmount(totalAmount);


        // Save order and order items
        Order savedOrder = orderRepository.save(order);


        // Clear customer's cart
        cart.getItems().clear();

        cartRepository.save(cart);


        return savedOrder;
    }


    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }


    @Override
    public List<Order> findByUser(User user) {
        return orderRepository.findByUser(user);
    }


    @Override
    public Order findById(long orderId) {

        return orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Order not found with id: " + orderId
                        )
                );
    }


    @Override
    public Order updateOrderStatus(long orderId, String status) {

        Order order = findById(orderId);

        order.setStatus(status);

        return orderRepository.save(order);
    }
    @Override
    public List<Order> findOrdersByFarmerId(long farmerId) {
        return orderRepository.findOrdersByFarmerId(farmerId);
    }

    @Override
    public void deleteById(long orderId) {

        if (!orderRepository.existsById(orderId)) {
            throw new RuntimeException(
                    "Order not found with id: " + orderId
            );
        }

        orderRepository.deleteById(orderId);
    }
}