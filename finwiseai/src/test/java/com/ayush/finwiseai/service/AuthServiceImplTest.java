package com.ayush.finwiseai.service;

import com.ayush.finwiseai.dto.request.LoginRequest;
import com.ayush.finwiseai.dto.request.RegisterRequest;
import com.ayush.finwiseai.dto.response.JwtResponse;
import com.ayush.finwiseai.entity.User;
import com.ayush.finwiseai.repository.UserRepository;
import com.ayush.finwiseai.security.JwtUtil;
import com.ayush.finwiseai.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Ayush");
        user.setEmail("ayush@test.com");
        user.setPassword("hashedPassword123");
    }

    @Test
    void register_ShouldHashPasswordAndReturnToken() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setName("Ayush");
        request.setEmail("ayush@test.com");
        request.setPassword("plainPassword123");

        when(userRepository.findByEmail("ayush@test.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("plainPassword123")).thenReturn("hashedPassword123");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtUtil.generateToken(any(User.class))).thenReturn("fake-jwt-token");

        // Act
        JwtResponse response = authService.register(request);

        // Assert
        assertThat(response.getToken()).isEqualTo("fake-jwt-token");
        assertThat(response.getEmail()).isEqualTo("ayush@test.com");

        // Verify password was actually encoded, not stored as plain text
        verify(passwordEncoder, times(1)).encode("plainPassword123");
    }

    @Test
    void register_WhenEmailAlreadyExists_ShouldThrowException() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setEmail("ayush@test.com");
        request.setPassword("password123");

        when(userRepository.findByEmail("ayush@test.com")).thenReturn(Optional.of(user));

        // Act & Assert
        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Email already registered");

        // Verify save was never attempted since email was duplicate
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_WithValidCredentials_ShouldReturnToken() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("ayush@test.com");
        request.setPassword("plainPassword123");

        when(userRepository.findByEmail("ayush@test.com")).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(user)).thenReturn("fake-jwt-token");

        // Act
        JwtResponse response = authService.login(request);

        // Assert
        assertThat(response.getToken()).isEqualTo("fake-jwt-token");
        assertThat(response.getUserId()).isEqualTo(1L);

        // Verify authentication was actually attempted
        verify(authenticationManager, times(1)).authenticate(any());
    }
}