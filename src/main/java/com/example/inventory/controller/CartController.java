package com.example.inventory.controller;

import com.example.inventory.entity.CartItem;
import com.example.inventory.entity.Product;
import com.example.inventory.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

import java.util.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final ProductRepository productRepo;

    public CartController(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    // Add item to cart
    @GetMapping("/add/{id}")
    public String addToCart(@PathVariable Long id, HttpSession session) {

        Product p = productRepo.findById(id).orElse(null);
        if (p == null) return "redirect:/products";

        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();

        // check if product already exists in cart
        for (CartItem item : cart) {
            if (item.getProductId().equals(id)) {
                item.setQuantity(item.getQuantity() + 1);
                session.setAttribute("cart", cart);
                return "redirect:/cart";
            }
        }

        cart.add(new CartItem(id, p.getProductName(), p.getPrice(), 1, p.getImageName()));
        session.setAttribute("cart", cart);

        return "redirect:/cart";
    }

    @SuppressWarnings("unchecked")
    @GetMapping
    public String viewCart(Model model, HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();

        BigDecimal total = cart.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("cartItems", cart);
        model.addAttribute("total", total);

        return "cart";
    }
    // Update quantity
    @PostMapping("/update/{id}")
    public String updateQuantity(@PathVariable Long id,
                                 @RequestParam int qty,
                                 HttpSession session) {

        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) return "redirect:/cart";

        for (CartItem item : cart) {
            if (item.getProductId().equals(id)) {
                item.setQuantity(qty);
            }
        }
        session.setAttribute("cart", cart);
        return "redirect:/cart";
    }

    // Remove item
    @GetMapping("/remove/{id}")
    public String removeItem(@PathVariable Long id, HttpSession session) {

        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart != null) {
            cart.removeIf(item -> item.getProductId().equals(id));
        }

        session.setAttribute("cart", cart);
        return "redirect:/cart";
    }

}
