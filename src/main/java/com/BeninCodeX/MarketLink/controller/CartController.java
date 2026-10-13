package com.BeninCodeX.MarketLink.controller;

import com.BeninCodeX.MarketLink.dto.CartUpdateRequest;
import com.BeninCodeX.MarketLink.entity.Cart;
import com.BeninCodeX.MarketLink.entity.CartItem;
import com.BeninCodeX.MarketLink.entity.Product;
import com.BeninCodeX.MarketLink.entity.User;
import com.BeninCodeX.MarketLink.service.CartItemService;
import com.BeninCodeX.MarketLink.service.CartService;
import com.BeninCodeX.MarketLink.service.ProductService;
import com.BeninCodeX.MarketLink.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final UserService userService;
    private final ProductService productService;

    public CartController(
            CartService cartService,
            CartItemService cartItemService,
            UserService userService,
            ProductService productService
    ) {
        this.cartService = cartService;
        this.cartItemService = cartItemService;
        this.userService = userService;
        this.productService = productService;
    }

    // =========================
    // GET USER CART
    // =========================

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserCart(@PathVariable long userId) {

        try {

            User user = userService.findById(userId);

            Cart cart = cartService.findByUser(user)
                    .orElseGet(() -> cartService.createCart(user));

            return ResponseEntity.ok(cart);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body("Unable to load cart: " + e.getMessage());
        }
    }


    // =========================
    // ADD ITEM TO CART
    // =========================

    @PostMapping("/item")
    public ResponseEntity<?> addToCart(
            @RequestBody CartUpdateRequest request
    ) {

        try {

            // 1. Find the user
            User user = userService.findById(request.getUserId());

            // 2. Find the product
            Product product = productService.findById(request.getProductId());

            // 3. Find the user's cart
            //    If the user doesn't have one, create it.
            Cart cart = cartService.findByUser(user)
                    .orElseGet(() -> cartService.createCart(user));

            // 4. Create CartItem
            CartItem cartItem = new CartItem();

            // IMPORTANT:
            // The cart cannot be null.
            cartItem.setCart(cart);

            // Set the product
            cartItem.setProduct(product);

            // Set quantity
            cartItem.setQuantity(request.getQuantity());

            // Set price
            cartItem.setPrice(request.getPrice());

            // 5. Save the CartItem
            CartItem savedItem = cartItemService.save(cartItem);

            return ResponseEntity.ok(savedItem);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }


    // =========================
    // UPDATE CART ITEM QUANTITY
    // =========================

    @PutMapping("/item")
    public ResponseEntity<?> updateCartItem(
            @RequestBody CartUpdateRequest request
    ) {

        try {

            CartItem updatedItem = cartItemService.updateQuantity(
                    request.getCartItemId(),
                    request.getUserId(),
                    request.getQuantity()
            );

            return ResponseEntity.ok(updatedItem);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }


    // =========================
    // DELETE CART ITEM
    // =========================

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<?> deleteCartItem(
            @PathVariable long cartItemId
    ) {

        try {

            cartItemService.deleteById(cartItemId);

            return ResponseEntity.ok(
                    "Cart item deleted successfully"
            );

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            "Unable to delete cart item: "
                                    + e.getMessage()
                    );
        }
    }
}