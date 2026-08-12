package com.example.demo.Model.Services;

import com.example.demo.Model.DTO.ChecklistDTO;
import com.example.demo.Model.DTO.ChecklistItemDTO;
import com.example.demo.Model.DTO.ChecklistResumenDTO;
import com.example.demo.Model.Entities.Checklist;
import com.example.demo.Model.Entities.ChecklistItem;
import com.example.demo.Model.Entities.JobOfferInfo;
import com.example.demo.Model.Entities.UsuarioEntity;
import com.example.demo.Model.Enums.Documento;
import com.example.demo.Model.Repository.ChecklistItemRepository;
import com.example.demo.Model.Repository.ChecklistRepository;
import com.example.demo.Model.Repository.JobOfferInfoRepository;
import com.example.demo.Model.Repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@AllArgsConstructor
@Service

public class ChecklistService {
    private final ChecklistRepository checklistRepository;
    private final ChecklistItemRepository checklistItemRepository;
    private final JobOfferInfoRepository jobOfferInfoRepository;
    private final UsuarioRepository usuarioRepository;


    @Transactional
    public ChecklistDTO crear(ChecklistDTO dto) {
        JobOfferInfo trabajo = jobOfferInfoRepository.findById(dto.getTrabajoId())
                .orElseThrow(() -> new EntityNotFoundException("Oferta de trabajo no encontrada"));

        UsuarioEntity usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        checklistRepository.findByTrabajoId(trabajo.getId()).ifPresent(c -> {
            throw new IllegalStateException("Ya existe un checklist para esta oferta de trabajo");
        });

        checklistRepository.findByUsuarioId(usuario.getId()).ifPresent(c -> {
            throw new IllegalStateException("El usuario ya tiene un checklist creado");
        });

        Checklist checklist = Checklist.builder()
                .trabajo(trabajo)
                .usuario(usuario)
                .build();

        Checklist guardado = checklistRepository.save(checklist);

        // auto-genera un item por cada documento del enum, sin completar
        List<ChecklistItem> items = Arrays.stream(Documento.values())
                .map(doc -> ChecklistItem.builder()
                        .documento(doc)
                        .completado(false)
                        .checklist(guardado)
                        .build())
                .collect(Collectors.toList());

        checklistItemRepository.saveAll(items);

        return ChecklistDTO.builder()
                .id(guardado.getId())
                .trabajoId(trabajo.getId())
                .usuarioId(usuario.getId())
                .build();
    }

    public ChecklistResumenDTO obtenerResumen(Long checklistId) {
        buscarOLanzar(checklistId); // valida que el checklist exista

        List<ChecklistItem> completados =
                checklistItemRepository.findByChecklistIdAndCompletado(checklistId, true);
        List<ChecklistItem> faltantes =
                checklistItemRepository.findByChecklistIdAndCompletado(checklistId, false);

        int total = completados.size() + faltantes.size();
        double progreso = total == 0 ? 0 : (double) completados.size() / total * 100;

        return ChecklistResumenDTO.builder()
                .checklistId(checklistId)
                .progreso(progreso)
                .completados(completados.stream().map(this::toItemDTO).collect(Collectors.toList()))
                .faltantes(faltantes.stream().map(this::toItemDTO).collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public ChecklistItemDTO marcarCompletado(Long itemId, boolean completado) {
        ChecklistItem item = checklistItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item no encontrado"));
        item.setCompletado(completado);
        return toItemDTO(item); // dirty checking: Hibernate guarda el cambio solo, dentro de la transacción
    }

    private Checklist buscarOLanzar(Long id) {
        return checklistRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Checklist no encontrado"));
    }

    private ChecklistItemDTO toItemDTO(ChecklistItem item) {
        return ChecklistItemDTO.builder()
                .id(item.getId())
                .documento(item.getDocumento())
                .completado(item.isCompletado())
                .fechaLimite(item.getFechaLimite())
                .build();
    }
}
