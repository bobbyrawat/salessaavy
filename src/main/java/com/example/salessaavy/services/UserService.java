package com.example.salessaavy.services;

import org.springframework.stereotype.Service;

import com.example.salessaavy.entity.User;
import com.example.salessaavy.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

   public void upgradeToSeller(String email) {

    User user = findByEmail(email);

    if ("SELLER".equalsIgnoreCase(user.getRole())) {
        return;
    }

    user.setRole("SELLER");

    userRepository.save(user);
}

    public User findByUsername(String username) {

    return userRepository.findByUsername(username)
            .orElseThrow(() ->
                    new RuntimeException("User not found"));
}
}