package com.example.salessaavy.services;

import com.example.salessaavy.dto.CartResponseDto;

public interface CartService {

    String addToCart(Long productId);

    CartResponseDto getCart();

    String increaseQuantity(Long productId);

    String decreaseQuantity(Long productId);

    String removeItem(Long productId);

    String clearCart();
}