package com.example.demo.Model.Controller;

import com.example.demo.Model.DTO.UsuarioResponseDTO;
import com.example.demo.Model.DTO.UsuarioUpdateDTO;
import com.example.demo.Model.Services.PerfilService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/perfil")
@RequiredArgsConstructor
public class PerfilController {
    private final PerfilService perfilService;

    @GetMapping
    public ResponseEntity<UsuarioResponseDTO> miPerfil(Authentication auth) {
        return ResponseEntity.ok(perfilService.obtenerPerfil(auth.getName()));
    }

    @DeleteMapping
    public ResponseEntity<Void> eliminarCuenta(Authentication auth) {
        perfilService.eliminarCuenta(auth.getName());
        return ResponseEntity.noContent().build();
    }
    @PutMapping
    public ResponseEntity<UsuarioResponseDTO> actualizarPerfil(
            Authentication auth,
            @Valid @RequestBody UsuarioUpdateDTO dto) {
        return ResponseEntity.ok(perfilService.actualizarPerfil(auth.getName(), dto));
    }
}
