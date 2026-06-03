package com.example.demo.Model.Auth;

import com.example.demo.Model.DTO.LoginRequestDTO;
import com.example.demo.Model.DTO.RegisterRequestDTO;
import com.example.demo.Model.DTO.UsuarioResponseDTO;

public interface AuthService {
    UsuarioResponseDTO login(LoginRequestDTO request);
    UsuarioResponseDTO register(RegisterRequestDTO request);
}