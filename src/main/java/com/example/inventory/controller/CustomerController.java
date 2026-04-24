package com.example.inventory.controller;

import com.example.inventory.entity.Customer;
import com.example.inventory.repository.CustomerRepository;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerRepository repo;
    public CustomerController(CustomerRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Customer> all() { return repo.findAll(); }

    @PostMapping
    public ResponseEntity<Customer> create(@RequestBody Customer c) {
        return new ResponseEntity<>(repo.save(c), HttpStatus.CREATED);
    }
}
