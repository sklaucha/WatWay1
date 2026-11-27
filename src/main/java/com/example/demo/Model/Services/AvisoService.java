package com.example.demo.Model.Services;

import com.example.demo.Model.DTO.AvisoDTO;
import com.example.demo.Model.DTO.CrearAvisoDTO;
import com.example.demo.Model.Entities.AvisoEntity;
import com.example.demo.Model.Eventos.AvisoPublicadoEvent;
import com.example.demo.Model.Repository.AvisoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Avisos generales. Al publicar uno, se dispara un evento (OBSERVER):
// el service no conoce a los que lo reciben -> Bajo Acoplamiento.
@Service
@RequiredArgsConstructor
public class AvisoService {

    private final AvisoRepository avisoRepository;
    private final UsuarioActualProvider usuarioActual;
    private final ApplicationEventPublisher eventos;

    // Para los usuarios: solo los activos
    public List<AvisoDTO> listarActivos() {
        return avisoRepository.findByActivoTrueOrderByFechaCreacionDesc().stream().map(this::toDTO).toList();
    }

    // Para el admin: todos
    public List<AvisoDTO> listarTodos() {
        return avisoRepository.findAllByOrderByFechaCreacionDesc().stream().map(this::toDTO).toList();
    }

    @Transactional
    public AvisoDTO crear(CrearAvisoDTO dto) {
        AvisoEntity aviso = avisoRepository.save(AvisoEntity.builder()
                .titulo(dto.getTitulo().trim())
                .mensaje(dto.getMensaje().trim())
                .importante(dto.isImportante())
                .autor(usuarioActual.getUsuario())
                .build());
        AvisoDTO creado = toDTO(aviso);
        eventos.publishEvent(new AvisoPublicadoEvent(creado));
        return creado;
    }

    @Transactional
    public AvisoDTO editar(Long id, CrearAvisoDTO dto) {
        AvisoEntity aviso = buscar(id);
        aviso.editar(dto.getTitulo(), dto.getMensaje(), dto.isImportante());
        return toDTO(avisoRepository.save(aviso));
    }

    // Prender o apagar sin borrarlo. Al volver a activarlo se avisa de nuevo.
    @Transactional
    public AvisoDTO cambiarActivo(Long id, boolean activo) {
        AvisoEntity aviso = buscar(id);
        boolean seActiva = activo && !aviso.isActivo();
        aviso.setActivo(activo);
        AvisoDTO actualizado = toDTO(avisoRepository.save(aviso));
        if (seActiva) eventos.publishEvent(new AvisoPublicadoEvent(actualizado));
        return actualizado;
    }

    public void eliminar(Long id) {
        avisoRepository.delete(buscar(id));
    }

    private AvisoEntity buscar(Long id) {
        return avisoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aviso no encontrado"));
    }

    private AvisoDTO toDTO(AvisoEntity a) {
        return AvisoDTO.builder()
                .id(a.getId())
                .titulo(a.getTitulo())
                .mensaje(a.getMensaje())
                .importante(a.isImportante())
                .activo(a.isActivo())
                .fechaCreacion(a.getFechaCreacion())
                .autorNombre(a.getAutor().getNombre() + " " + a.getAutor().getApellido())
                .build();
    }
}
