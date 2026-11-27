package com.example.demo.Model.Services.denuncias;

import com.example.demo.Model.Entities.UsuarioEntity;
import com.example.demo.Model.Enums.TipoDenuncia;
import com.example.demo.Model.Excepciones.UsuarioNotFoundException;
import com.example.demo.Model.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// Denuncia de un perfil (por ejemplo, un perfil falso)
@Component
@RequiredArgsConstructor
public class ManejadorDenunciaUsuario implements ManejadorDenuncia {

    private final UsuarioRepository usuarioRepository;

    @Override
    public TipoDenuncia tipo() {
        return TipoDenuncia.USUARIO;
    }

    @Override
    public ContenidoDenunciado buscar(Long usuarioId, UsuarioEntity actual) {
        UsuarioEntity u = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado"));
        String texto = "Perfil de " + u.getNombre() + " " + u.getApellido()
                + (u.getDescripcion() != null && !u.getDescripcion().isBlank() ? ": " + u.getDescripcion() : "");
        return new ContenidoDenunciado(u, texto);
    }

    @Override
    public void borrar(Long referenciaId) {
        // Un perfil no se "borra": la medida es desactivar al usuario
    }

    @Override
    public boolean tieneContenidoBorrable() {
        return false;
    }
}
