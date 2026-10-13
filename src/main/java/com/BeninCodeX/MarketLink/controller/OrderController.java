package com.BeninCodeX.MarketLink.controller;

import com.BeninCodeX.MarketLink.entity.Order;
import com.BeninCodeX.MarketLink.entity.User;
import com.BeninCodeX.MarketLink.service.OrderService;
import com.BeninCodeX.MarketLink.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(
            OrderService orderService,
            UserService userService
    ) {
        this.orderService = orderService;
        this.userService = userService;
    }


    // CUSTOMER CREATES ORDER FROM CART
    @PostMapping
    public Order createOrder(
            @RequestBody Map<String, Object> request
    ) {

        long userId = Long.parseLong(
                request.get("userId").toString()
        );

        String pickupPlace =
                request.get("pickupPlace").toString();

        return orderService.createOrder(
                userId,
                pickupPlace
        );
    }




    // GET ONE ORDER
    @GetMapping("/{orderId}")
    public Order getOrderById(
            @PathVariable long orderId
    ) {

        return orderService.findById(orderId);
    }


    // GET CUSTOMER'S ORDERS
    @GetMapping("/user/{userId}")
    public List<Order> getOrdersByUser(
            @PathVariable long userId
    ) {

        User user = userService.findById(userId);

        return orderService.findByUser(user);
    }


    // ACCEPT / DECLINE ORDER
    @PutMapping("/{orderId}/status")
    public Order updateOrderStatus(
            @PathVariable long orderId,
            @RequestParam String status
    ) {

        return orderService.updateOrderStatus(
                orderId,
                status
        );
    }

    @GetMapping("/farmer/{farmerId}")
    public List<Order> getFarmerOrders(
            @PathVariable long farmerId
    ) {
        return orderService.findOrdersByFarmerId(farmerId);
    }

    // DELETE ORDER
    @DeleteMapping("/{orderId}")
    public String deleteOrder(
            @PathVariable long orderId
    ) {

        orderService.deleteById(orderId);

        return "Order deleted successfully";
    }
}