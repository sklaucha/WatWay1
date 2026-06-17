package com.example.demo.Model.Controller;

import com.example.demo.Model.DTO.EnviarMensajeDTO;
import com.example.demo.Model.DTO.MensajeDTO;
import com.example.demo.Model.Services.MensajeService;
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
    public ResponseEntity<MensajeDTO> enviarMensaje(@RequestBody EnviarMensajeDTO request) {
        return ResponseEntity.ok(mensajeService.enviarMensaje(request));
    }

    @GetMapping("/conversacion/{usuarioId}")
    public ResponseEntity<List<MensajeDTO>> getConversacion(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(mensajeService.getConversacion(usuarioId));
    }
}