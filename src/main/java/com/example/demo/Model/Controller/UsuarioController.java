package com.example.demo.Model.Controller;

import com.example.demo.Model.DTO.ActualizarUsuarioDTO;
import com.example.demo.Model.DTO.CambiarPasswordDTO;
import com.example.demo.Model.DTO.UsuarioResponseDTO;
import com.example.demo.Model.Services.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // Ya no existe GET /usuarios: devolvía las entidades completas de todos
    // (con email y hash de contraseña). Para listar usuarios está GET /admin/usuarios,
    // solo para ADMIN y con un DTO que no incluye la contraseña.

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
