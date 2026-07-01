package com.ayush.finwiseai.service.impl;

import com.ayush.finwiseai.dto.request.LoginRequest;
import com.ayush.finwiseai.dto.request.RegisterRequest;
import com.ayush.finwiseai.dto.response.JwtResponse;
import com.ayush.finwiseai.entity.User;
import com.ayush.finwiseai.repository.UserRepository;
import com.ayush.finwiseai.security.JwtUtil;
import com.ayush.finwiseai.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtResponse register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        String token = jwtUtil.generateToken(savedUser);

        return new JwtResponse(token, "Bearer", savedUser.getId(), savedUser.getName(), savedUser.getEmail());
    }

    @Override
    public JwtResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generateToken(user);

        return new JwtResponse(token, "Bearer", user.getId(), user.getName(), user.getEmail());
    }
}