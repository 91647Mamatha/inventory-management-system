package com.example.inventory.controller;

import com.example.inventory.entity.Product;
import com.example.inventory.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepo;

    public ProductController(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    // GET: all products
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    // GET: single product by id
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return productRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST: create new product
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        // ensure id is null so JPA treats it as new
        product.setProductId(null);
        Product saved = productRepo.save(product);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // PUT: update full product
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id,
                                                 @RequestBody Product updated) {

        return productRepo.findById(id).map(existing -> {
            existing.setProductName(updated.getProductName());
            existing.setCategory(updated.getCategory());
            existing.setPrice(updated.getPrice());
            existing.setQuantity(updated.getQuantity());
            existing.setImageName(updated.getImageName());
            Product saved = productRepo.save(existing);
            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    // PATCH: update only quantity
    @PatchMapping("/{id}/quantity")
    public ResponseEntity<Product> updateQuantity(@PathVariable Long id,
                                                  @RequestParam Integer quantity) {

        return productRepo.findById(id).map(existing -> {
            existing.setQuantity(quantity);
            Product saved = productRepo.save(existing);
            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE: delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (!productRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
