package com.example.demo.Model.Services.denuncias;

import com.example.demo.Model.Entities.UsuarioEntity;
import com.example.demo.Model.Enums.TipoDenuncia;

// STRATEGY (GoF) + POLIMORFISMO (GRASP): cada tipo de denuncia sabe
// buscar su contenido y borrarlo a su manera. DenunciaService no tiene
// un "if (tipo == MENSAJE) ... else if ...": le pide al manejador del tipo.
// Agregar un tipo nuevo (ej: GASTO) = una clase nueva -> OCP.
public interface ManejadorDenuncia {

    TipoDenuncia tipo();

    // Busca lo denunciado. "actual" es quien denuncia (para validar que lo pueda ver)
    ContenidoDenunciado buscar(Long referenciaId, UsuarioEntity actual);

    // Borra el contenido (si el tipo tiene contenido para borrar)
    void borrar(Long referenciaId);

    // USUARIO no tiene contenido: devuelve false
    default boolean tieneContenidoBorrable() {
        return true;
    }
}
