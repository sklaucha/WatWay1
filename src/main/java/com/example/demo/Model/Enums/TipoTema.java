package com.example.demo.Model.Enums;

// POLIMORFISMO (GRASP) / ABIERTO-CERRADO (SOLID):
// cada tipo de tema sabe sus propias reglas. El service no pregunta
// "if (tipo == FAQ)": le pide la respuesta al tipo. Si mañana aparece
// un tipo nuevo (ej: ANUNCIO), se agrega acá sin tocar el service.
public enum TipoTema {

    // Tema que abre cualquier usuario: lo edita su autor y admite respuestas
    NORMAL {
        @Override
        public boolean admiteRespuestas() { return true; }

        @Override
        public boolean puedeEditar(boolean esAutor, boolean esAdmin) { return esAutor; }
    },

    // Pregunta frecuente: la edita cualquier admin y no lleva respuestas
    FAQ {
        @Override
        public boolean admiteRespuestas() { return false; }

        @Override
        public boolean puedeEditar(boolean esAutor, boolean esAdmin) { return esAdmin; }
    };

    public abstract boolean admiteRespuestas();

    public abstract boolean puedeEditar(boolean esAutor, boolean esAdmin);
}
