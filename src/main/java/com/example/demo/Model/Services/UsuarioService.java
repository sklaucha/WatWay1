package com.example.demo.Model.Services;

import com.example.demo.Model.DTO.RegisterRequestDTO;
import com.example.demo.Model.DTO.UsuarioResponseDTO;

import java.util.List;

public interface UsuarioService {

    UsuarioResponseDTO crearUsuario(RegisterRequestDTO request);

    UsuarioResponseDTO obtenerUsuario(Long id);

    List<UsuarioResponseDTO> listarUsuarios();

    void eliminarUsuario(Long id);

    void eliminarCuenta();
}