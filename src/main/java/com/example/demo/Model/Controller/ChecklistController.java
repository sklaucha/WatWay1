package com.example.demo.Model.Controller;

import com.example.demo.Model.DTO.ChecklistDTO;
import com.example.demo.Model.DTO.ChecklistItemDTO;
import com.example.demo.Model.DTO.ChecklistResumenDTO;
import com.example.demo.Model.Services.ChecklistService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/checklist")
public class ChecklistController {
    private final ChecklistService checklistService;

    @PostMapping
    public ResponseEntity<ChecklistDTO> crear(@Valid @RequestBody ChecklistDTO dto) {
        return ResponseEntity.ok(checklistService.crear(dto));
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
}
