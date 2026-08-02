package com.example.salessaavy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.salessaavy.dto.CartResponseDto;
import com.example.salessaavy.services.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // 1. Add product to cart
    @PostMapping("/add/{productId}")
    public ResponseEntity<String> addToCart(@PathVariable Long productId) {
        return ResponseEntity.ok(cartService.addToCart(productId));
    }

    // 2. View cart
    @GetMapping
    public ResponseEntity<CartResponseDto> getCart() {
        return ResponseEntity.ok(cartService.getCart());
    }

    // 3. Increase quantity
    @PutMapping("/increase/{productId}")
    public ResponseEntity<String> increaseQuantity(@PathVariable Long productId) {
        return ResponseEntity.ok(cartService.increaseQuantity(productId));
    }

    // 4. Decrease quantity
    @PutMapping("/decrease/{productId}")
    public ResponseEntity<String> decreaseQuantity(@PathVariable Long productId) {
        return ResponseEntity.ok(cartService.decreaseQuantity(productId));
    }

    // 5. Remove item from cart
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<String> removeItem(@PathVariable Long productId) {
        return ResponseEntity.ok(cartService.removeItem(productId));
    }

    // 6. Clear cart
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart() {
        return ResponseEntity.ok(cartService.clearCart());
    }
}