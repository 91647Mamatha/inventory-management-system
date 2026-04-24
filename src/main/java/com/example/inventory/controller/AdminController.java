package com.example.inventory.controller;

import com.example.inventory.repository.CustomerRepository;
import com.example.inventory.repository.OrderRepository;
import com.example.inventory.repository.ProductRepository;
import com.example.inventory.repository.SupplierRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final OrderRepository orderRepo;
    private final CustomerRepository customerRepo;
    private final ProductRepository productRepo;
    private final SupplierRepository supplierRepo;

    public AdminController(OrderRepository orderRepo,
                           CustomerRepository customerRepo,
                           ProductRepository productRepo,
                           SupplierRepository supplierRepo) {
        this.orderRepo = orderRepo;
        this.customerRepo = customerRepo;
        this.productRepo = productRepo;
        this.supplierRepo = supplierRepo;
    }

    @GetMapping
    public String adminRoot() {
        return "redirect:/admin/dashboard";
    }

    // ✅ FIXED DASHBOARD METHOD
    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        // Existing counts (no change)
        model.addAttribute("orderCount", orderRepo.count());
        model.addAttribute("productCount", productRepo.count());
        model.addAttribute("customerCount", customerRepo.count());
        model.addAttribute("supplierCount", supplierRepo.count());

        // 🔥 ADDED THIS PART (MAIN FIX)
        model.addAttribute("products", productRepo.findAll());
        model.addAttribute("customers", customerRepo.findAll());
        model.addAttribute("suppliers", supplierRepo.findAll());
        model.addAttribute("orders", orderRepo.findAll());

        return "admin-dashboard";
    }

    @GetMapping("/products")
    public String products(Model model) {
        model.addAttribute("products", productRepo.findAll());
        return "admin-products";
    }

    @GetMapping("/customers")
    public String customers(Model model) {
        model.addAttribute("customers", customerRepo.findAll());
        return "admin-customers";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        model.addAttribute("orders", orderRepo.findAll());
        return "admin-orders";
    }

    @GetMapping("/suppliers")
    public String suppliers(Model model) {

        System.out.println("🔥 SUPPLIERS PAGE HIT");

        model.addAttribute("suppliers", supplierRepo.findAll());

        return "admin-suppliers";
    }
}