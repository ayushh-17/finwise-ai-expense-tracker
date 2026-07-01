package com.ayush.finwiseai.service;

import com.ayush.finwiseai.dto.request.LoginRequest;
import com.ayush.finwiseai.dto.request.RegisterRequest;
import com.ayush.finwiseai.dto.response.JwtResponse;

public interface AuthService {
    JwtResponse register(RegisterRequest request);
    JwtResponse login(LoginRequest request);
}