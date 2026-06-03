package com.example.demo.Model.Auth;

import com.example.demo.Model.DTO.LoginRequestDTO;
import com.example.demo.Model.DTO.RegisterRequestDTO;
import com.example.demo.Model.DTO.UsuarioResponseDTO;
import com.example.demo.Model.Entities.UsuarioEntity;
import com.example.demo.Model.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder; // inyectado desde ApplicationConfig

    @Override
    public UsuarioResponseDTO login(LoginRequestDTO request) {
        UsuarioEntity usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword()))
            throw new RuntimeException("Contraseña incorrecta");

        return toDTO(usuario);
    }

    @Override
    public UsuarioResponseDTO register(RegisterRequestDTO request) {
        if (usuarioRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("El email ya está registrado");

        UsuarioEntity usuario = UsuarioEntity.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .posicionLaboral(request.getPosicionLaboral())
                .ciudadDestino(request.getCiudadDestino())
                .sexo(request.getSexo())
                .descripcion(request.getDescripcion())
                .build();

        usuarioRepository.save(usuario);
        return toDTO(usuario);
    }

    private UsuarioResponseDTO toDTO(UsuarioEntity u) {
        return UsuarioResponseDTO.builder()
                .id(u.getId())
                .nombre(u.getNombre())
                .apellido(u.getApellido())
                .email(u.getEmail())
                .posicionLaboral(u.getPosicionLaboral())
                .ciudadDestino(u.getCiudadDestino())
                .sexo(u.getSexo())
                .descripcion(u.getDescripcion())
                .build();
    }
}