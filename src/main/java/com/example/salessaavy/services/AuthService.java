package com.example.salessaavy.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.salessaavy.dto.AuthResponse;
import com.example.salessaavy.dto.LoginRequest;
import com.example.salessaavy.dto.RegisterRequest;
import com.example.salessaavy.entity.User;
import com.example.salessaavy.repository.UserRepository;



import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

  public void register(RegisterRequest request) {

    if (userRepository.existsByUsername(request.getUsername())) {
        throw new RuntimeException("Username already exists");
    }

    if (userRepository.existsByEmail(request.getEmail())) {
        throw new RuntimeException("Email already exists");
    }

    User user = new User();

    user.setName(request.getName());
    user.setUsername(request.getUsername());
    user.setEmail(request.getEmail());
    user.setPassword(
            passwordEncoder.encode(request.getPassword())
    );
    user.setRole("USER");

    userRepository.save(user);
}

    public AuthResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetails user = (UserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(user);

        User dbUser = userRepository
        .findByUsername(request.getUsername())
        .orElseThrow();

return new AuthResponse(
        token,
        dbUser.getUsername(),
        dbUser.getRole()
);
    }
}