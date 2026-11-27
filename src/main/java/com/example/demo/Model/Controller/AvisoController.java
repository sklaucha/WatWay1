package com.example.demo.Model.Controller;

import com.example.demo.Model.DTO.ActivoDTO;
import com.example.demo.Model.DTO.AvisoDTO;
import com.example.demo.Model.DTO.CrearAvisoDTO;
import com.example.demo.Model.Services.AvisoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Avisos generales
//  Usuarios: GET /avisos/activos
//  Admin:    GET/POST /admin/avisos, PUT/DELETE /admin/avisos/{id}, PUT /admin/avisos/{id}/activo
@RestController
@RequiredArgsConstructor
public class AvisoController {

    private final AvisoService avisoService;

    @GetMapping("/avisos/activos")
    public ResponseEntity<List<AvisoDTO>> activos() {
        return ResponseEntity.ok(avisoService.listarActivos());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin/avisos")
    public ResponseEntity<List<AvisoDTO>> todos() {
        return ResponseEntity.ok(avisoService.listarTodos());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/admin/avisos")
    public ResponseEntity<AvisoDTO> crear(@Valid @RequestBody CrearAvisoDTO dto) {
        return ResponseEntity.status(201).body(avisoService.crear(dto));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/admin/avisos/{id}")
    public ResponseEntity<AvisoDTO> editar(@PathVariable Long id, @Valid @RequestBody CrearAvisoDTO dto) {
        return ResponseEntity.ok(avisoService.editar(id, dto));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/admin/avisos/{id}/activo")
    public ResponseEntity<AvisoDTO> cambiarActivo(@PathVariable Long id, @Valid @RequestBody ActivoDTO dto) {
        return ResponseEntity.ok(avisoService.cambiarActivo(id, dto.getActivo()));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/admin/avisos/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        avisoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
