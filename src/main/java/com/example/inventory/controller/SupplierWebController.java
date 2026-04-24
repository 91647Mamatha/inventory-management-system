package com.example.inventory.controller;

import com.example.inventory.entity.Supplier;
import com.example.inventory.repository.SupplierRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/suppliers")
public class SupplierWebController {

    private final SupplierRepository supplierRepo;

    public SupplierWebController(SupplierRepository supplierRepo) {
        this.supplierRepo = supplierRepo;
    }

    @PostMapping("/save")
    public String saveSupplier(@ModelAttribute Supplier supplier) {
        supplierRepo.save(supplier);
        return "redirect:/admin/suppliers";
    }
}