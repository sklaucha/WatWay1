package com.example.demo.Model.Services.denuncias;

import com.example.demo.Model.Entities.ForoTemaEntity;
import com.example.demo.Model.Entities.UsuarioEntity;
import com.example.demo.Model.Enums.TipoDenuncia;
import com.example.demo.Model.Repository.ForoRespuestaRepository;
import com.example.demo.Model.Repository.ForoTemaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// Denuncia de un tema del foro
@Component
@RequiredArgsConstructor
public class ManejadorDenunciaTemaForo implements ManejadorDenuncia {

    private final ForoTemaRepository foroTemaRepository;
    private final ForoRespuestaRepository foroRespuestaRepository;

    @Override
    public TipoDenuncia tipo() {
        return TipoDenuncia.TEMA_FORO;
    }

    @Override
    public ContenidoDenunciado buscar(Long temaId, UsuarioEntity actual) {
        ForoTemaEntity t = foroTemaRepository.findById(temaId)
                .orElseThrow(() -> new EntityNotFoundException("Tema no encontrado"));
        return new ContenidoDenunciado(t.getAutor(), t.getTitulo() + ": " + t.getContenido());
    }

    // Se borra el tema con todas sus respuestas (igual que cuando lo borra su autor)
    @Override
    public void borrar(Long temaId) {
        foroTemaRepository.findById(temaId).ifPresent(t -> {
            foroRespuestaRepository.deleteByTema_Id(temaId);
            foroTemaRepository.delete(t);
        });
    }
}
