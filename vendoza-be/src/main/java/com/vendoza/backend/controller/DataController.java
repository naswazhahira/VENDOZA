package com.vendoza.backend.controller;

import com.vendoza.backend.entity.Product;
import com.vendoza.backend.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/data")
@CrossOrigin(origins = "*")
public class DataController {

    @Autowired
    private DataService dataService;

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return dataService.getAllProducts();
    }

    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable Long id) {
        return dataService.getProductById(id);
    }

    @GetMapping("/products/category/{category}")
    public List<Product> getProductsByCategory(@PathVariable String category) {
        return dataService.getProductsByCategory(category);
    }

    @GetMapping("/products/search")
    public List<Product> searchProducts(@RequestParam String keyword) {
        return dataService.searchProducts(keyword);
    }

    @PostMapping("/products")
    public Product addProduct(@RequestBody Product product) {
        return dataService.saveProduct(product);
    }

    @DeleteMapping("/products/{id}")
    public String deleteProduct(@PathVariable Long id) {
        dataService.deleteProduct(id);
        return "Product deleted successfully!";
    }

    // ========== TAMBAHAN ENDPOINT UNTUK UPDATE PRODUCT ==========

    @PutMapping("/products/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        Product existing = dataService.getProductById(id);

        if (updatedProduct.getName() != null) {
            existing.setName(updatedProduct.getName());
        }
        if (updatedProduct.getPrice() != null) {
            existing.setPrice(updatedProduct.getPrice());
        }
        if (updatedProduct.getDescription() != null) {
            existing.setDescription(updatedProduct.getDescription());
        }
        if (updatedProduct.getImageUrl() != null) {
            existing.setImageUrl(updatedProduct.getImageUrl());
        }
        if (updatedProduct.getCategory() != null) {
            existing.setCategory(updatedProduct.getCategory());
        }
        if (updatedProduct.getStock() != null) {
            existing.setStock(updatedProduct.getStock());
        }

        return dataService.saveProduct(existing);
    }
}