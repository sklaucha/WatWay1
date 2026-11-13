package com.example.demo.AdminGastos.Controller;

import com.example.demo.AdminGastos.DTO.MovimientoDTO;
import com.example.demo.AdminGastos.Entitys.ExpensesEntity;
import com.example.demo.AdminGastos.Service.ExpensesService;
import com.example.demo.Model.Entities.UsuarioEntity;
import com.example.demo.Model.Repository.UsuarioRepository;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpensesController {
    @Autowired
    private ExpensesService expensesService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private UsuarioEntity usuarioActual(Authentication auth) {
        return usuarioRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
    }

    @GetMapping
    public List<MovimientoDTO> listar(Authentication auth) {
        return expensesService.listar(usuarioActual(auth));
    }

    @PostMapping
    public MovimientoDTO crear(@Valid @RequestBody MovimientoDTO dto, Authentication auth) {
        return expensesService.crear(dto, usuarioActual(auth));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, Authentication auth) {
        return expensesService.eliminar(id, usuarioActual(auth))
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
