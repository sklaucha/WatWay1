package com.example.demo.Seguridad.Services;

import com.example.demo.Model.DTO.LoginRequestDTO;
import com.example.demo.Model.DTO.RegisterRequestDTO;
import com.example.demo.Model.Entities.UsuarioEntity;
import com.example.demo.Model.Repository.UsuarioRepository;
import com.example.demo.Seguridad.DTO.AuthResponse;
import com.example.demo.Seguridad.Entities.CredentialsEntity;
import com.example.demo.Seguridad.Repositories.CredentialsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final CredentialsRepository credentialsRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse login(LoginRequestDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Contraseña incorrecta");
        }

        CredentialsEntity credentials = credentialsRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return new AuthResponse(
                jwtService.generateToken(credentials),
                jwtService.generateRefreshToken(credentials)
        );
    }

    @Override
    public AuthResponse register(RegisterRequestDTO request) {
        if (credentialsRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("El email ya está registrado");

        // Guardar datos del usuario
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

        // Guardar credenciales de seguridad
        CredentialsEntity credentials = CredentialsEntity.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .usuario(usuario)
                .build();
        credentialsRepository.save(credentials);

        return new AuthResponse(
                jwtService.generateToken(credentials),
                jwtService.generateRefreshToken(credentials)
        );
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        return credentialsRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
    }
}