package com.example.salessaavy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.salessaavy.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByUserUsername(String username);

    List<Product> findByUserUsernameAndNameContainingIgnoreCase(
            String username,
            String name);

}