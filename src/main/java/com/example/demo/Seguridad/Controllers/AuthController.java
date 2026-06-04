package com.example.demo.Seguridad.Controllers;

import com.example.demo.Seguridad.DTO.AuthResponse;
import com.example.demo.Seguridad.DTO.RefreshTokenRequest;
import com.example.demo.Seguridad.Services.AuthService;
import com.example.demo.Seguridad.Services.JwtService;
import com.example.demo.Seguridad.Entities.CredentialsEntity;
import com.example.demo.Model.Repository.UsuarioRepository;
import com.example.demo.Model.DTO.LoginRequestDTO;
import com.example.demo.Model.DTO.RegisterRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequestDTO request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshTokenRequest request) {
        String email = jwtService.extractUsername(request.getRefreshToken());
        CredentialsEntity credentials = (CredentialsEntity) authService.loadUserByUsername(email);

        if (!jwtService.isTokenValid(request.getRefreshToken(), credentials))
            return ResponseEntity.status(401).build();

        return ResponseEntity.ok(new AuthResponse(
                jwtService.generateToken(credentials),
                jwtService.generateRefreshToken(credentials)
        ));
    }
}