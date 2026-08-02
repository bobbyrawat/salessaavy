package com.example.salessaavy.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.salessaavy.dto.CartItemResponseDto;
import com.example.salessaavy.dto.CartResponseDto;
import com.example.salessaavy.entity.Cart;
import com.example.salessaavy.entity.CartItem;
import com.example.salessaavy.entity.Product;
import com.example.salessaavy.entity.User;
import com.example.salessaavy.repository.CartItemRepository;
import com.example.salessaavy.repository.CartRepository;
import com.example.salessaavy.repository.ProductRepository;
import com.example.salessaavy.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    // ------------------ USER ------------------
    private User getLoggedInUser() {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ------------------ CART ------------------
    private Cart getOrCreateCart(User user) {

        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    cart.setCartItems(new ArrayList<>());
                    return cartRepository.save(cart);
                });
    }

    // ------------------ ADD TO CART ------------------
    @Override
    public String addToCart(Long productId) {

        User user = getLoggedInUser();
        Cart cart = getOrCreateCart(user);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem cartItem = cartItemRepository
                .findByCartAndProduct(cart, product)
                .orElse(null);

        if (cartItem != null) {

            if (product.getQuantity() <= cartItem.getQuantity()) {
                throw new RuntimeException("Not enough stock available");
            }

            cartItem.setQuantity(cartItem.getQuantity() + 1);

        } else {

            if (product.getQuantity() <= 0) {
                throw new RuntimeException("Out of stock");
            }

            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(1);

            // 🔥 CRITICAL FIX (this was missing in your code)
            cart.getCartItems().add(cartItem);
        }

        cartItemRepository.save(cartItem);
        cartRepository.save(cart);

        return "Product added to cart successfully";
    }

    // ------------------ GET CART ------------------
    @Override
    public CartResponseDto getCart() {

        User user = getLoggedInUser();

        // safer fresh fetch (avoids stale collection issue)
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> getOrCreateCart(user));

        List<CartItemResponseDto> itemDtos = new ArrayList<>();

        int totalQuantity = 0;
        double totalAmount = 0;

        for (CartItem item : cart.getCartItems()) {

            Product product = item.getProduct();

            double subtotal = product.getPrice() * item.getQuantity();

            CartItemResponseDto dto = new CartItemResponseDto();

            dto.setProductId(product.getId());
            dto.setProductName(product.getName());
            dto.setProductImage(product.getPhotoUrl());
            dto.setPrice(product.getPrice());
            dto.setQuantity(item.getQuantity());
            dto.setSubtotal(subtotal);

            itemDtos.add(dto);

            totalQuantity += item.getQuantity();
            totalAmount += subtotal;
        }

        CartResponseDto response = new CartResponseDto();

        response.setItems(itemDtos);
        response.setTotalQuantity(totalQuantity);
        response.setTotalAmount(totalAmount);

        return response;
    }

    // ------------------ INCREASE ------------------
    @Override
    public String increaseQuantity(Long productId) {

        User user = getLoggedInUser();
        Cart cart = getOrCreateCart(user);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem cartItem = cartItemRepository
                .findByCartAndProduct(cart, product)
                .orElseThrow(() -> new RuntimeException("Product not in cart"));

        if (product.getQuantity() <= cartItem.getQuantity()) {
            throw new RuntimeException("Not enough stock available");
        }

        cartItem.setQuantity(cartItem.getQuantity() + 1);
        cartItemRepository.save(cartItem);

        return "Quantity increased successfully";
    }

    // ------------------ DECREASE ------------------
    @Override
    public String decreaseQuantity(Long productId) {

        User user = getLoggedInUser();
        Cart cart = getOrCreateCart(user);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem cartItem = cartItemRepository
                .findByCartAndProduct(cart, product)
                .orElseThrow(() -> new RuntimeException("Product not in cart"));

        if (cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            cartItemRepository.save(cartItem);
        } else {
            cartItemRepository.delete(cartItem);
        }

        return "Quantity updated successfully";
    }

    // ------------------ REMOVE ITEM ------------------
    @Override
    public String removeItem(Long productId) {

        User user = getLoggedInUser();
        Cart cart = getOrCreateCart(user);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem cartItem = cartItemRepository
                .findByCartAndProduct(cart, product)
                .orElseThrow(() -> new RuntimeException("Product not in cart"));

        cartItemRepository.delete(cartItem);

        return "Item removed from cart";
    }

    // ------------------ CLEAR CART ------------------
    @Override
    public String clearCart() {

        User user = getLoggedInUser();
        Cart cart = getOrCreateCart(user);

        cartItemRepository.deleteAll(cart.getCartItems());

        cart.getCartItems().clear();

        cartRepository.save(cart);

        return "Cart cleared successfully";
    }
}