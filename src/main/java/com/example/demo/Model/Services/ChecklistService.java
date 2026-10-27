package com.example.demo.Model.Services;

import com.example.demo.Model.DTO.ChecklistItemDTO;
import com.example.demo.Model.DTO.ChecklistResumenDTO;
import com.example.demo.Model.Entities.Checklist;
import com.example.demo.Model.Entities.ChecklistItem;
import com.example.demo.Model.Entities.UsuarioEntity;
import com.example.demo.Model.Enums.Documento;
import com.example.demo.Model.Excepciones.UsuarioNotFoundException;
import com.example.demo.Model.Repository.ChecklistItemRepository;
import com.example.demo.Model.Repository.ChecklistRepository;
import com.example.demo.Model.Repository.UsuarioRepository;
import com.example.demo.Seguridad.Entities.CredentialsEntity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ChecklistService {
    private final ChecklistRepository checklistRepository;
    private final ChecklistItemRepository checklistItemRepository;
    private final UsuarioRepository usuarioRepository;

    // ---------- Usuario logueado ----------

    private UsuarioEntity getUsuarioActual() {
        CredentialsEntity credentials = (CredentialsEntity)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return usuarioRepository.findByEmail(credentials.getEmail())
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado"));
    }

    private void verificarPropietario(Checklist checklist) {
        if (!checklist.getUsuario().getId().equals(getUsuarioActual().getId())) {
            throw new EntityNotFoundException("Checklist no encontrado");
        }
    }

    // ---------- Resumen ----------

    // Si el usuario todavía no tiene checklist, se crea con todos los documentos sin marcar
    @Transactional
    public ChecklistResumenDTO obtenerMiResumen() {
        UsuarioEntity actual = getUsuarioActual();
        Checklist checklist = checklistRepository.findByUsuarioId(actual.getId())
                .orElseGet(() -> crearParaUsuario(actual));
        return armarResumen(checklist.getId());
    }

    public ChecklistResumenDTO obtenerResumen(Long checklistId) {
        Checklist checklist = buscarOLanzar(checklistId);
        verificarPropietario(checklist);
        return armarResumen(checklistId);
    }

    // ---------- Marcar ----------

    @Transactional
    public ChecklistItemDTO marcarCompletado(Long itemId, boolean completado) {
        ChecklistItem item = checklistItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item no encontrado"));
        verificarPropietario(item.getChecklist());
        item.setCompletado(completado);
        return toItemDTO(item);
    }

    // ---------- Helpers ----------

    private Checklist crearParaUsuario(UsuarioEntity usuario) {
        Checklist guardado = checklistRepository.save(
                Checklist.builder().usuario(usuario).build());

        List<ChecklistItem> items = Arrays.stream(Documento.values())
                .map(doc -> ChecklistItem.builder()
                        .documento(doc)
                        .completado(false)
                        .checklist(guardado)
                        .build())
                .collect(Collectors.toList());

        checklistItemRepository.saveAll(items);
        return guardado;
    }

    private ChecklistResumenDTO armarResumen(Long checklistId) {
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