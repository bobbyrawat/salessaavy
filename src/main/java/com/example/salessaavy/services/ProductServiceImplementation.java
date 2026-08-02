package com.example.salessaavy.services;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.salessaavy.entity.Product;
import com.example.salessaavy.entity.User;
import com.example.salessaavy.repository.ProductRepository;
import com.example.salessaavy.repository.UserRepository;
@Service
public class ProductServiceImplementation
        implements ProductService {


    private final ProductRepository repo;
private final UserRepository userRepository;

   public ProductServiceImplementation(
        ProductRepository repo,
        UserRepository userRepository) {

    this.repo = repo;
    this.userRepository = userRepository;
}

@Override
public String addProduct(Product prod) {

    System.out.println("Logged User = " +
        SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());

                System.out.println("Authentication = " +
        SecurityContextHolder
                .getContext()
                .getAuthentication());

    String username =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getName();

    User user =
            userRepository
                    .findByUsername(username)
                    .orElseThrow();

    prod.setUser(user);

    repo.save(prod);

    return "Product added successfully";
}
    @Override
    public String updateProduct(Product product) {

        Product existing =
                repo.findById(product.getId())
                        .orElse(null);

        if (existing == null) {
            return "Product Not Found";
        }

        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setQuantity(product.getQuantity());
        existing.setPhotoUrl(product.getPhotoUrl());

        repo.save(existing);

        return "Product Updated Successfully";
    }

    @Override
    public String deleteProduct(Long productId) {

        if (!repo.existsById(productId)) {
            return "Product Not Found";
        }

        repo.deleteById(productId);

        return "Product Deleted Successfully";
    }

    @Override
    public Product viewProduct(Long productId) {

        return repo.findById(productId)
                .orElse(null);
    }

    @Override
public List<Product> viewAllProducts() {
    return repo.findAll();
}

  @Override
public List<Product> searchProducts(String keyword) {

    String username = SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getName();

    if (keyword == null || keyword.trim().isEmpty()) {
        return repo.findByUserUsername(username);
    }

    return repo.findByUserUsernameAndNameContainingIgnoreCase(
            username,
            keyword.trim());
}

    @Override
public List<Product> viewMyProducts() {

    String username =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getName();

    return repo.findByUserUsername(username);
}

    
}