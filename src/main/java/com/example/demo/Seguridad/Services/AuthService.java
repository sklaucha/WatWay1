package com.example.demo.Seguridad.Services;

import com.example.demo.Seguridad.DTO.AuthResponse;
import com.example.demo.Model.DTO.LoginRequestDTO;
import com.example.demo.Model.DTO.RegisterRequestDTO;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {
    AuthResponse login(LoginRequestDTO request);
    AuthResponse register(RegisterRequestDTO request);
    UserDetails loadUserByUsername(String email);
}