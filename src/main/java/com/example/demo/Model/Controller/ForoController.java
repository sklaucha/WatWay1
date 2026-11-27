package com.example.demo.Model.Controller;

import com.example.demo.Model.DTO.*;
import com.example.demo.Model.Services.ForoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Foro: un foro por estado (mapa) + un foro global con preguntas frecuentes.
// CRUD de temas:      POST/GET/PUT/DELETE  /foro/.../temas  y  /foro/temas/{id}
// CRUD de respuestas: POST /foro/temas/{id}/respuestas  ·  PUT/DELETE /foro/respuestas/{id}
@RestController
@RequestMapping("/foro")
@RequiredArgsConstructor
public class ForoController {

    private final ForoService foroService;

    // ---------- Mapa ----------

    @GetMapping("/estados")
    public ResponseEntity<List<EstadoForoDTO>> listarEstados() {
        return ResponseEntity.ok(foroService.listarEstados());
    }

    @GetMapping("/estados/{codigo}/temas")
    public ResponseEntity<List<TemaResumenDTO>> temasDeEstado(@PathVariable String codigo) {
        return ResponseEntity.ok(foroService.temasDeEstado(codigo));
    }

    @PostMapping("/estados/{codigo}/temas")
    public ResponseEntity<TemaDetalleDTO> crearTemaEnEstado(@PathVariable String codigo,
                                                            @Valid @RequestBody CrearTemaDTO dto) {
        return ResponseEntity.status(201).body(foroService.crearTemaEnEstado(codigo, dto));
    }

    // ---------- Foro global ----------

    @GetMapping("/global/temas")
    public ResponseEntity<List<TemaResumenDTO>> temasGlobales() {
        return ResponseEntity.ok(foroService.temasGlobales());
    }

    @PostMapping("/global/temas")
    public ResponseEntity<TemaDetalleDTO> crearTemaGlobal(@Valid @RequestBody CrearTemaDTO dto) {
        return ResponseEntity.status(201).body(foroService.crearTemaGlobal(dto));
    }

    // Solo el admin crea preguntas frecuentes
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/global/faq")
    public ResponseEntity<TemaDetalleDTO> crearFaq(@Valid @RequestBody CrearTemaDTO dto) {
        return ResponseEntity.status(201).body(foroService.crearFaq(dto));
    }

    // ---------- Un tema ----------

    @GetMapping("/temas/{id}")
    public ResponseEntity<TemaDetalleDTO> obtenerTema(@PathVariable Long id) {
        return ResponseEntity.ok(foroService.obtenerTema(id));
    }

    @PutMapping("/temas/{id}")
    public ResponseEntity<TemaDetalleDTO> editarTema(@PathVariable Long id,
                                                     @Valid @RequestBody CrearTemaDTO dto) {
        return ResponseEntity.ok(foroService.editarTema(id, dto));
    }

    @DeleteMapping("/temas/{id}")
    public ResponseEntity<Void> eliminarTema(@PathVariable Long id) {
        foroService.eliminarTema(id);
        return ResponseEntity.noContent().build();
    }

    // ---------- Respuestas ----------

    @PostMapping("/temas/{id}/respuestas")
    public ResponseEntity<RespuestaDTO> responder(@PathVariable Long id,
                                                  @Valid @RequestBody CrearRespuestaDTO dto) {
        return ResponseEntity.status(201).body(foroService.responder(id, dto));
    }

    @PutMapping("/respuestas/{id}")
    public ResponseEntity<RespuestaDTO> editarRespuesta(@PathVariable Long id,
                                                        @Valid @RequestBody CrearRespuestaDTO dto) {
        return ResponseEntity.ok(foroService.editarRespuesta(id, dto));
    }

    @DeleteMapping("/respuestas/{id}")
    public ResponseEntity<Void> eliminarRespuesta(@PathVariable Long id) {
        foroService.eliminarRespuesta(id);
        return ResponseEntity.noContent().build();
    }
}
