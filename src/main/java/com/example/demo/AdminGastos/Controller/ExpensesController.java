package com.example.demo.AdminGastos.Controller;

import com.example.demo.AdminGastos.DTO.MovimientoDTO;
import com.example.demo.AdminGastos.Service.ExpensesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// CRUD de movimientos (gastos e ingresos) del usuario logueado
@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpensesController {

    private final ExpensesService expensesService;

    // Listar -> GET /api/expenses
    @GetMapping
    public ResponseEntity<List<MovimientoDTO>> listar() {
        return ResponseEntity.ok(expensesService.listar());
    }

    // Crear -> POST /api/expenses
    @PostMapping
    public ResponseEntity<MovimientoDTO> crear(@Valid @RequestBody MovimientoDTO dto) {
        return ResponseEntity.status(201).body(expensesService.crear(dto));
    }

    // Editar -> PUT /api/expenses/{id}
    @PutMapping("/{id}")
    public ResponseEntity<MovimientoDTO> actualizar(@PathVariable Long id,
                                                    @Valid @RequestBody MovimientoDTO dto) {
        return ResponseEntity.ok(expensesService.actualizar(id, dto));
    }

    // Borrar -> DELETE /api/expenses/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        expensesService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
