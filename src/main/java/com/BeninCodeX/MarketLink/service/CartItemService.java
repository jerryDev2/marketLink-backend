package com.BeninCodeX.MarketLink.service;

import com.BeninCodeX.MarketLink.entity.Cart;
import com.BeninCodeX.MarketLink.entity.CartItem;
import com.BeninCodeX.MarketLink.repository.CartItemRepository;
import com.BeninCodeX.MarketLink.repository.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;

    public CartItemService(
            CartItemRepository cartItemRepository,
            CartRepository cartRepository
    ) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
    }

    public CartItem save(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    public List<CartItem> findAll() {
        return cartItemRepository.findAll();
    }

    public CartItem findById(long cartItemId) {
        return cartItemRepository.findById(cartItemId)
                .orElseThrow(() ->
                        new RuntimeException("Cart item not found: " + cartItemId));
    }

    @Transactional
    public CartItem updateQuantity(
            long cartItemId,
            long userId,
            long quantity
    ) {

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() ->
                        new RuntimeException("Cart item not found")
                );

        if (quantity < 1) {
            throw new RuntimeException("Quantity must be at least 1");
        }

        cartItem.setQuantity(quantity);

        return cartItemRepository.save(cartItem);
    }


    public void deleteById(long orderId) {

        if (!cartItemRepository.existsById(orderId)) {
            throw new RuntimeException(
                    "Order not found with id: " + orderId
            );
        }

        cartItemRepository.deleteById(orderId);
    }
}