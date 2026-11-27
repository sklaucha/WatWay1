package com.example.demo.Model.Services;

import com.example.demo.Model.DTO.EditarMensajeDTO;
import com.example.demo.Model.DTO.EnviarMensajeDTO;
import com.example.demo.Model.DTO.MensajeDTO;
import com.example.demo.Model.DTO.NoLeidosDTO;
import com.example.demo.Model.Entities.MensajeEntity;
import com.example.demo.Model.Entities.UsuarioEntity;
import com.example.demo.Model.Excepciones.AccionNoPermitidaException;
import com.example.demo.Model.Excepciones.UsuarioNotFoundException;
import com.example.demo.Model.Repository.MatchRepository;
import com.example.demo.Model.Repository.MensajeRepository;
import com.example.demo.Model.Repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MensajeService {

    private final MensajeRepository mensajeRepository;
    private final UsuarioRepository usuarioRepository;
    private final MatchRepository matchRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final UsuarioActualProvider usuarioActual;

    public MensajeDTO enviarMensaje(EnviarMensajeDTO request) {
        UsuarioEntity emisor = usuarioActual.getUsuario();
        UsuarioEntity receptor = usuarioRepository.findById(request.getReceptorId())
                .orElseThrow(() -> new UsuarioNotFoundException("Receptor no encontrado"));

        // Verificar que tengan match
        boolean tienenMatch =
                matchRepository.existsByUsuarioOrigenAndUsuarioDestinoAndMatched(emisor, receptor, true) ||
                        matchRepository.existsByUsuarioOrigenAndUsuarioDestinoAndMatched(receptor, emisor, true);

        if (!tienenMatch) {
            throw new IllegalArgumentException("Solo podés chatear con tus matches.");
        }

        MensajeEntity mensaje = MensajeEntity.builder()
                .emisor(emisor)
                .receptor(receptor)
                .contenido(request.getContenido().trim())
                .build();

        mensajeRepository.save(mensaje);

        // Enviar en tiempo real al receptor (desde su punto de vista no puede modificarlo)
        messagingTemplate.convertAndSendToUser(
                receptor.getEmail(),
                "/queue/mensajes",
                toDTO(mensaje, receptor)
        );

        return toDTO(mensaje, emisor);
    }

    // Abrir el chat = leer: los mensajes que me mandó el otro quedan como leídos.
    // Desde ese momento quien los mandó ya no los puede editar ni borrar.
    @Transactional
    public List<MensajeDTO> getConversacion(Long otroUsuarioId) {
        UsuarioEntity actual = usuarioActual.getUsuario();
        UsuarioEntity otro = usuarioRepository.findById(otroUsuarioId)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado"));

        List<MensajeEntity> sinLeer = mensajeRepository.findByEmisorAndReceptorAndLeidoFalse(otro, actual);
        sinLeer.forEach(MensajeEntity::marcarLeido);
        mensajeRepository.saveAll(sinLeer);

        return mensajeRepository.findConversacion(actual, otro)
                .stream()
                .map(m -> toDTO(m, actual))
                .toList();
    }

    // Cuántos mensajes sin leer tengo en cada chat
    public List<NoLeidosDTO> contarNoLeidos() {
        return mensajeRepository.contarNoLeidosPorEmisor(usuarioActual.getUsuario());
    }

    // Solo quien mandó el mensaje puede editarlo o borrarlo.
    // Si es de otro, respondemos "no encontrado" para no dar pistas.
    // Si ya fue leído o borrado -> 403 con el motivo.
    private MensajeEntity buscarMensajeModificable(Long mensajeId, String accion) {
        UsuarioEntity actual = usuarioActual.getUsuario();
        MensajeEntity mensaje = mensajeRepository.findById(mensajeId)
                .orElseThrow(() -> new EntityNotFoundException("Mensaje no encontrado"));
        if (!mensaje.esEmisor(actual))
            throw new EntityNotFoundException("Mensaje no encontrado");
        if (mensaje.isEliminado())
            throw new AccionNoPermitidaException("El mensaje ya fue eliminado");
        if (!mensaje.puedeModificar(actual))
            throw new AccionNoPermitidaException("No podés " + accion + " un mensaje que ya fue leído");
        return mensaje;
    }

    public MensajeDTO editarMensaje(Long mensajeId, EditarMensajeDTO request) {
        MensajeEntity mensaje = buscarMensajeModificable(mensajeId, "editar");
        mensaje.editar(request.getContenido());
        return toDTO(mensajeRepository.save(mensaje), usuarioActual.getUsuario());
    }

    // Borrado lógico: el otro ve "Mensaje eliminado"
    public void eliminarMensaje(Long mensajeId) {
        MensajeEntity mensaje = buscarMensajeModificable(mensajeId, "borrar");
        mensaje.eliminar();
        mensajeRepository.save(mensaje);
    }

    // "actual" es quien va a ver el DTO: define si puede modificar el mensaje
    private MensajeDTO toDTO(MensajeEntity m, UsuarioEntity actual) {
        return MensajeDTO.builder()
                .id(m.getId())
                .emisorId(m.getEmisor().getId())
                .emisorNombre(m.getEmisor().getNombre())
                .receptorId(m.getReceptor().getId())
                .receptorNombre(m.getReceptor().getNombre())
                .contenido(m.isEliminado() ? null : m.getContenido())
                .fechaEnvio(m.getFechaEnvio())
                .editado(m.getFechaEdicion() != null)
                .fechaEdicion(m.getFechaEdicion())
                .leido(m.isLeido())
                .fechaLectura(m.getFechaLectura())
                .eliminado(m.isEliminado())
                .puedeModificar(m.puedeModificar(actual))
                .build();
    }
}
