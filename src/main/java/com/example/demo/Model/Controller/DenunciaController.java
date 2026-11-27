package com.example.demo.Model.Controller;

import com.example.demo.Model.DTO.CrearDenunciaDTO;
import com.example.demo.Model.DTO.DenunciaDTO;
import com.example.demo.Model.DTO.ResolverDenunciaDTO;
import com.example.demo.Model.Enums.EstadoDenuncia;
import com.example.demo.Model.Services.DenunciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// Denuncias
//  Usuarios: POST /denuncias, GET /denuncias/mias
//  Admin:    GET /admin/denuncias?estado=, GET /admin/denuncias/pendientes,
//            PUT /admin/denuncias/{id}/revisar | /resolver | /descartar
@RestController
@RequiredArgsConstructor
public class DenunciaController {

    private final DenunciaService denunciaService;

    @PostMapping("/denuncias")
    public ResponseEntity<DenunciaDTO> crear(@Valid @RequestBody CrearDenunciaDTO dto) {
        return ResponseEntity.status(201).body(denunciaService.crear(dto));
    }

    @GetMapping("/denuncias/mias")
    public ResponseEntity<List<DenunciaDTO>> mias() {
        return ResponseEntity.ok(denunciaService.misDenuncias());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin/denuncias")
    public ResponseEntity<List<DenunciaDTO>> listar(@RequestParam(required = false) EstadoDenuncia estado) {
        return ResponseEntity.ok(denunciaService.listar(estado));
    }

    // Para el numerito del panel: { "pendientes": 3 }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin/denuncias/pendientes")
    public ResponseEntity<Map<String, Long>> pendientes() {
        return ResponseEntity.ok(Map.of("pendientes", denunciaService.contarPendientes()));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/admin/denuncias/{id}/revisar")
    public ResponseEntity<DenunciaDTO> revisar(@PathVariable Long id) {
        return ResponseEntity.ok(denunciaService.revisar(id));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/admin/denuncias/{id}/resolver")
    public ResponseEntity<DenunciaDTO> resolver(@PathVariable Long id, @Valid @RequestBody ResolverDenunciaDTO dto) {
        return ResponseEntity.ok(denunciaService.resolver(id, dto));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/admin/denuncias/{id}/descartar")
    public ResponseEntity<DenunciaDTO> descartar(@PathVariable Long id,
                                                 @Valid @RequestBody(required = false) ResolverDenunciaDTO dto) {
        return ResponseEntity.ok(denunciaService.descartar(id, dto));
    }
}
