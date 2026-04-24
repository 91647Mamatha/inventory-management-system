package com.example.inventory.controller;

import com.example.inventory.entity.CartItem;
import com.example.inventory.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    private final OrderService orderService;

    public CheckoutController(OrderService orderService) {
        this.orderService = orderService;
    }

    // show checkout page
    @GetMapping
    public String showCheckoutPage() {
        return "checkout";
    }

    // process order
    @PostMapping("/place")
    public String placeOrder(@RequestParam String customerName,
                             @RequestParam String phone,
                             @RequestParam String address,
                             HttpSession session) {

        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            return "redirect:/cart";
        }

        orderService.saveOrder(customerName, address, phone, cart);

        session.removeAttribute("cart");

        return "redirect:/order-success";
    }
}
