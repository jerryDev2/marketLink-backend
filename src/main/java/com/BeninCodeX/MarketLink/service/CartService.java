package com.BeninCodeX.MarketLink.service;

import com.BeninCodeX.MarketLink.entity.Cart;
import com.BeninCodeX.MarketLink.entity.User;
import com.BeninCodeX.MarketLink.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart createCart(User user) {

        Cart cart = new Cart();
        cart.setUser(user);

        return cartRepository.save(cart);
    }

    public Optional<Cart> findByUser(User user) {
        return cartRepository.findByUser(user);
    }

    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }

    public Cart findById(long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found"));
    }

    public void deleteById(long cartId) {
        cartRepository.deleteById(cartId);
    }
}