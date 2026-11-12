package com.example.demo.Model.Controller;

import com.example.demo.Model.DTO.EditarMensajeDTO;
import com.example.demo.Model.DTO.EnviarMensajeDTO;
import com.example.demo.Model.DTO.MensajeDTO;
import com.example.demo.Model.Services.MensajeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final MensajeService mensajeService;

    @PostMapping("/enviar")
    public ResponseEntity<MensajeDTO> enviarMensaje(@Valid @RequestBody EnviarMensajeDTO request) {
        return ResponseEntity.ok(mensajeService.enviarMensaje(request));
    }

    @GetMapping("/conversacion/{usuarioId}")
    public ResponseEntity<List<MensajeDTO>> getConversacion(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(mensajeService.getConversacion(usuarioId));
    }

    // ---------- CRUD de mensajes ----------
    // Crear  -> POST   /chat/enviar
    // Leer   -> GET    /chat/conversacion/{usuarioId}
    // Editar -> PUT    /chat/mensajes/{mensajeId}
    // Borrar -> DELETE /chat/mensajes/{mensajeId}

    @PutMapping("/mensajes/{mensajeId}")
    public ResponseEntity<MensajeDTO> editarMensaje(@PathVariable Long mensajeId,
                                                    @Valid @RequestBody EditarMensajeDTO request) {
        return ResponseEntity.ok(mensajeService.editarMensaje(mensajeId, request));
    }

    @DeleteMapping("/mensajes/{mensajeId}")
    public ResponseEntity<Void> eliminarMensaje(@PathVariable Long mensajeId) {
        mensajeService.eliminarMensaje(mensajeId);
        return ResponseEntity.noContent().build();
    }
}