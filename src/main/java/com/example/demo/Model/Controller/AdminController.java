package com.example.demo.Model.Controller;

import com.example.demo.Model.Services.AdminService;
import com.example.demo.Model.Services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UsuarioService usuarioService;
    private final AdminService adminService;

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("Sos ADMIN");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.ok("Usuario eliminado correctamente.");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/usuarios/{credentialId}/rol/{rol}")
    public ResponseEntity<String> asignarRol(@PathVariable Long credentialId,
                                             @PathVariable String rol) {
        adminService.asignarRol(credentialId, rol);
        return ResponseEntity.ok("Rol asignado correctamente.");
    }
}