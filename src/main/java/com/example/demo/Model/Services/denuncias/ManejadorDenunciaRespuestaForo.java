package com.example.demo.Model.Services.denuncias;

import com.example.demo.Model.Entities.ForoRespuestaEntity;
import com.example.demo.Model.Entities.UsuarioEntity;
import com.example.demo.Model.Enums.TipoDenuncia;
import com.example.demo.Model.Repository.ForoRespuestaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// Denuncia de una respuesta del foro
@Component
@RequiredArgsConstructor
public class ManejadorDenunciaRespuestaForo implements ManejadorDenuncia {

    private final ForoRespuestaRepository foroRespuestaRepository;

    @Override
    public TipoDenuncia tipo() {
        return TipoDenuncia.RESPUESTA_FORO;
    }

    @Override
    public ContenidoDenunciado buscar(Long respuestaId, UsuarioEntity actual) {
        ForoRespuestaEntity r = foroRespuestaRepository.findById(respuestaId)
                .orElseThrow(() -> new EntityNotFoundException("Respuesta no encontrada"));
        return new ContenidoDenunciado(r.getAutor(), r.getContenido());
    }

    @Override
    public void borrar(Long respuestaId) {
        foroRespuestaRepository.findById(respuestaId).ifPresent(foroRespuestaRepository::delete);
    }
}
