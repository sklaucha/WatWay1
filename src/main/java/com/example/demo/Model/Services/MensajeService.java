package com.example.demo.Model.Services;

import com.example.demo.Model.DTO.EnviarMensajeDTO;
import com.example.demo.Model.DTO.MensajeDTO;
import com.example.demo.Model.Entities.MensajeEntity;
import com.example.demo.Model.Entities.UsuarioEntity;
import com.example.demo.Model.Excepciones.UsuarioNotFoundException;
import com.example.demo.Model.Repository.MatchRepository;
import com.example.demo.Model.Repository.MensajeRepository;
import com.example.demo.Model.Repository.UsuarioRepository;
import com.example.demo.Seguridad.Entities.CredentialsEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MensajeService {

    private final MensajeRepository mensajeRepository;
    private final UsuarioRepository usuarioRepository;
    private final MatchRepository matchRepository;
    private final SimpMessagingTemplate messagingTemplate;

    private UsuarioEntity getUsuarioActual() {
        CredentialsEntity credentials = (CredentialsEntity)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return usuarioRepository.findByEmail(credentials.getEmail())
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado"));
    }

    public MensajeDTO enviarMensaje(EnviarMensajeDTO request) {
        UsuarioEntity emisor = getUsuarioActual();
        UsuarioEntity receptor = usuarioRepository.findById(request.getReceptorId())
                .orElseThrow(() -> new UsuarioNotFoundException("Receptor no encontrado"));

        // Verificar que tengan match
        boolean tienenMatch =
                matchRepository.existsByUsuarioOrigenAndUsuarioDestinoAndMatched(emisor, receptor, true) ||
                        matchRepository.existsByUsuarioOrigenAndUsuarioDestinoAndMatched(receptor, emisor, true);

        if (!tienenMatch) {
            throw new RuntimeException("Solo podés chatear con tus matches.");
        }

        MensajeEntity mensaje = MensajeEntity.builder()
                .emisor(emisor)
                .receptor(receptor)
                .contenido(request.getContenido())
                .build();

        mensajeRepository.save(mensaje);

        MensajeDTO mensajeDTO = toDTO(mensaje);

        // Enviar en tiempo real al receptor
        messagingTemplate.convertAndSendToUser(
                receptor.getEmail(),
                "/queue/mensajes",
                mensajeDTO
        );

        return mensajeDTO;
    }

    public List<MensajeDTO> getConversacion(Long otroUsuarioId) {
        UsuarioEntity actual = getUsuarioActual();
        UsuarioEntity otro = usuarioRepository.findById(otroUsuarioId)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado"));

        return mensajeRepository.findConversacion(actual, otro)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private MensajeDTO toDTO(MensajeEntity m) {
        return MensajeDTO.builder()
                .id(m.getId())
                .emisorId(m.getEmisor().getId())
                .emisorNombre(m.getEmisor().getNombre())
                .receptorId(m.getReceptor().getId())
                .receptorNombre(m.getReceptor().getNombre())
                .contenido(m.getContenido())
                .fechaEnvio(m.getFechaEnvio())
                .build();
    }
}