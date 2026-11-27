package com.example.demo.Model.Services.denuncias;

import com.example.demo.Model.Entities.MensajeEntity;
import com.example.demo.Model.Entities.UsuarioEntity;
import com.example.demo.Model.Enums.TipoDenuncia;
import com.example.demo.Model.Repository.MensajeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// Denuncia de un mensaje del chat
@Component
@RequiredArgsConstructor
public class ManejadorDenunciaMensaje implements ManejadorDenuncia {

    private final MensajeRepository mensajeRepository;

    @Override
    public TipoDenuncia tipo() {
        return TipoDenuncia.MENSAJE;
    }

    // Solo se puede denunciar un mensaje que te mandaron a vos.
    // Si no, "no encontrado": no se da información de chats ajenos.
    @Override
    public ContenidoDenunciado buscar(Long mensajeId, UsuarioEntity actual) {
        MensajeEntity m = mensajeRepository.findById(mensajeId)
                .orElseThrow(() -> new EntityNotFoundException("Mensaje no encontrado"));
        if (!m.getReceptor().getId().equals(actual.getId()))
            throw new EntityNotFoundException("Mensaje no encontrado");
        if (m.isEliminado())
            throw new IllegalArgumentException("Ese mensaje ya fue eliminado");
        return new ContenidoDenunciado(m.getEmisor(), m.getContenido());
    }

    // Borrado lógico: en el chat queda "Mensaje eliminado"
    @Override
    public void borrar(Long mensajeId) {
        mensajeRepository.findById(mensajeId).ifPresent(m -> {
            m.eliminar();
            mensajeRepository.save(m);
        });
    }
}
