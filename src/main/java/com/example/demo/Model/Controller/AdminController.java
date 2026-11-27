package com.example.demo.Model.Controller;

import com.example.demo.Model.DTO.AdminUsuarioDTO;
import com.example.demo.Model.DTO.EmpleadoresDTO;
import com.example.demo.Model.DTO.EstadoForoDTO;
import com.example.demo.Model.Services.AdminService;
import com.example.demo.Model.Services.ForoService;
import com.example.demo.Model.Services.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// hasAuthority('ADMIN') y no hasRole('ADMIN'):
// hasRole busca "ROLE_ADMIN", pero el token guarda el rol como "ADMIN".
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UsuarioService usuarioService;
    private final AdminService adminService;
    private final ForoService foroService;

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("Sos ADMIN");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/usuarios")
    public ResponseEntity<List<AdminUsuarioDTO>> listarUsuarios() {
        return ResponseEntity.ok(adminService.listarUsuarios());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.ok("Usuario eliminado correctamente.");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/usuarios/{id}/activar")
    public ResponseEntity<String> reactivarUsuario(@PathVariable Long id) {
        adminService.reactivarUsuario(id);
        return ResponseEntity.ok("Usuario reactivado correctamente.");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/usuarios/{credentialId}/rol/{rol}")
    public ResponseEntity<String> asignarRol(@PathVariable Long credentialId,
                                             @PathVariable String rol) {
        adminService.asignarRol(credentialId, rol);
        return ResponseEntity.ok("Rol asignado correctamente.");
    }

    // Quitar un rol (por ejemplo ADMIN). Reglas en AdminService.quitarRol
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/usuarios/{credentialId}/rol/{rol}")
    public ResponseEntity<String> quitarRol(@PathVariable Long credentialId,
                                            @PathVariable String rol) {
        adminService.quitarRol(credentialId, rol);
        return ResponseEntity.ok("Rol quitado correctamente.");
    }

    // Marca o desmarca si la agencia tiene empleadores en un estado (pinta el mapa del foro)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/estados/{codigo}/empleadores")
    public ResponseEntity<EstadoForoDTO> marcarEmpleadores(@PathVariable String codigo,
                                                           @Valid @RequestBody EmpleadoresDTO dto) {
        return ResponseEntity.ok(foroService.marcarEmpleadores(codigo, dto.getTieneEmpleadores()));
    }
}
