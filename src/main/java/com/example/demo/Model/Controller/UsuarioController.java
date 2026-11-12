package com.example.demo.Model.Controller;

import com.example.demo.Model.DTO.ActualizarUsuarioDTO;
import com.example.demo.Model.DTO.CambiarPasswordDTO;
import com.example.demo.Model.DTO.UsuarioResponseDTO;
import com.example.demo.Model.Entities.UsuarioEntity;
import com.example.demo.Model.Repository.UsuarioRepository;
import com.example.demo.Model.Services.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;

    @GetMapping
    public List<UsuarioEntity> getAll() {
        return usuarioRepository.findAll();
    }

    // ---------- CRUD del perfil propio ----------
    // Crear  -> POST   /auth/register
    // Leer   -> GET    /usuarios/me
    // Editar -> PUT    /usuarios/me
    // Borrar -> DELETE /auth/baja

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> miPerfil() {
        return ResponseEntity.ok(usuarioService.obtenerMiPerfil());
    }

    @PutMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> actualizarMiPerfil(@Valid @RequestBody ActualizarUsuarioDTO request) {
        return ResponseEntity.ok(usuarioService.actualizarMiPerfil(request));
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> cambiarPassword(@Valid @RequestBody CambiarPasswordDTO request) {
        usuarioService.cambiarPassword(request);
        return ResponseEntity.noContent().build();
    }
}
