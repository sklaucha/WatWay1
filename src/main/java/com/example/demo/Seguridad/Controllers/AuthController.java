    package com.example.demo.Seguridad.Controllers;

    import com.example.demo.Seguridad.DTO.AuthResponse;
    import com.example.demo.Seguridad.DTO.RefreshTokenRequest;
    import com.example.demo.Seguridad.Services.AuthService;
    import com.example.demo.Seguridad.Services.JwtService;
    import com.example.demo.Seguridad.Services.TokenBlacklistService;
    import com.example.demo.Seguridad.Entities.CredentialsEntity;
    import com.example.demo.Model.DTO.LoginRequestDTO;
    import com.example.demo.Model.DTO.RegisterRequestDTO;
    import com.example.demo.Model.Services.UsuarioService;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/auth")
    @RequiredArgsConstructor
    public class AuthController {

        private final AuthService authService;
        private final JwtService jwtService;
        private final TokenBlacklistService tokenBlacklistService;
        private final UsuarioService usuarioService;

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

        @PostMapping("/logout")
        public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader,
                                             @RequestBody RefreshTokenRequest request) {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                tokenBlacklistService.blacklist(authHeader.substring(7));
            }
            if (request.getRefreshToken() != null) {
                tokenBlacklistService.blacklist(request.getRefreshToken());
            }
            return ResponseEntity.ok("Sesión cerrada correctamente.");
        }

        @DeleteMapping("/baja")
        public ResponseEntity<String> eliminarCuenta(@RequestHeader("Authorization") String authHeader,
                                                     @RequestBody RefreshTokenRequest request) {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                tokenBlacklistService.blacklist(authHeader.substring(7));
            }
            if (request.getRefreshToken() != null) {
                tokenBlacklistService.blacklist(request.getRefreshToken());
            }
            usuarioService.eliminarCuenta();
            return ResponseEntity.ok("Cuenta eliminada correctamente.");
        }
    }