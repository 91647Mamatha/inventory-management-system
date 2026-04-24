package com.example.inventory.controller;

import com.example.inventory.entity.Supplier;
import com.example.inventory.repository.SupplierRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@CrossOrigin
public class SupplierController {

    private final SupplierRepository repo;

    public SupplierController(SupplierRepository repo) {
        this.repo = repo;
    }

    // GET all
    @GetMapping
    public List<Supplier> getAll() {
        return repo.findAll();
    }

    // POST create
    @PostMapping
    public Supplier save(@RequestBody Supplier supplier) {
        return repo.save(supplier);
    }
}