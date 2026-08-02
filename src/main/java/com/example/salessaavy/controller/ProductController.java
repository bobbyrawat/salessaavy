package com.example.salessaavy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.salessaavy.entity.Product;
import com.example.salessaavy.services.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

 @PostMapping("/add")
public ResponseEntity<String> addProduct(
        @RequestBody Product product) {

    System.out.println("===== CONTROLLER HIT =====");
    System.out.println(product);

    String result = productService.addProduct(product);

    System.out.println("===== SERVICE RETURNED =====");
    System.out.println(result);

    return ResponseEntity.ok(result);
}

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProduct(
            @PathVariable Long id,
            @RequestBody Product product) {

        product.setId(id);

        return ResponseEntity.ok(
                productService.updateProduct(product));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                productService.deleteProduct(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> viewProduct(
            @PathVariable Long id) {

        Product product =
                productService.viewProduct(id);

        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(product);
    }

    @GetMapping
    public ResponseEntity<List<Product>>
    viewAllProducts() {

        return ResponseEntity.ok(
                productService.viewAllProducts());
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<Product>>
    searchProducts(
            @PathVariable String keyword) {

        return ResponseEntity.ok(
                productService.searchProducts(keyword));
    }

    @GetMapping("/my-products")
public ResponseEntity<List<Product>> viewMyProducts() {

    return ResponseEntity.ok(
            productService.viewMyProducts());
}
}