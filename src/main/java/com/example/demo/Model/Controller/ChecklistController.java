package com.example.demo.Model.Controller;

import com.example.demo.Model.DTO.ChecklistItemDTO;
import com.example.demo.Model.DTO.ChecklistResumenDTO;
import com.example.demo.Model.Services.ChecklistService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/checklist")
public class ChecklistController {
    private final ChecklistService checklistService;

    @GetMapping("/mi-resumen")
    public ResponseEntity<ChecklistResumenDTO> obtenerMiResumen() {
        return ResponseEntity.ok(checklistService.obtenerMiResumen());
    }

    @GetMapping("/{checklistId}/resumen")
    public ResponseEntity<ChecklistResumenDTO> obtenerResumen(@PathVariable Long checklistId) {
        return ResponseEntity.ok(checklistService.obtenerResumen(checklistId));
    }

    @PatchMapping("/items/{itemId}/completado")
    public ResponseEntity<ChecklistItemDTO> marcarCompletado(
            @PathVariable Long itemId,
            @RequestParam boolean completado) {
        return ResponseEntity.ok(checklistService.marcarCompletado(itemId, completado));
    }

    // ---------- CRUD de items ----------
    // Crear  -> POST   /checklist/items
    // Leer   -> GET    /checklist/mi-resumen
    // Editar -> PUT    /checklist/items/{itemId}
    // Borrar -> DELETE /checklist/items/{itemId}

    @PostMapping("/items")
    public ResponseEntity<ChecklistItemDTO> agregarItem(@RequestBody ChecklistItemDTO request) {
        return ResponseEntity.status(201).body(checklistService.agregarItem(request));
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<ChecklistItemDTO> actualizarItem(@PathVariable Long itemId,
                                                           @RequestBody ChecklistItemDTO request) {
        return ResponseEntity.ok(checklistService.actualizarItem(itemId, request));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> eliminarItem(@PathVariable Long itemId) {
        checklistService.eliminarItem(itemId);
        return ResponseEntity.noContent().build();
    }
}