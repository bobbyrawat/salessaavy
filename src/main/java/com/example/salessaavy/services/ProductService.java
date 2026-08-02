package com.example.salessaavy.services;

import java.util.List;

import com.example.salessaavy.entity.Product;

public interface ProductService {

    String addProduct(Product product);

    String updateProduct(Product product);

    String deleteProduct(Long productId);

    Product viewProduct(Long productId);

    List<Product> viewAllProducts();

    List<Product> searchProducts(String keyword);

    List<Product> viewMyProducts();
}