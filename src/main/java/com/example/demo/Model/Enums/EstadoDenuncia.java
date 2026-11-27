package com.example.demo.Model.Enums;

import com.example.demo.Model.Excepciones.TransicionInvalidaException;

// STATE (GoF): cada estado de la denuncia decide qué se puede hacer y a qué
// estado se pasa. No hay ningún switch (estado) en el service ni en la entidad:
// la denuncia le pregunta a su estado actual.
//
//   PENDIENTE ──revisar──> EN_REVISION ──resolver──> RESUELTA
//       │                       │
//       └──────descartar────────┴────descartar─────> DESCARTADA
//
// RESUELTA y DESCARTADA son finales: no aceptan más cambios.
public enum EstadoDenuncia {

    PENDIENTE {
        @Override public EstadoDenuncia revisar() { return EN_REVISION; }
        @Override public EstadoDenuncia resolver() {
            throw new TransicionInvalidaException("Primero hay que tomar la denuncia en revisión");
        }
        @Override public EstadoDenuncia descartar() { return DESCARTADA; }
    },

    EN_REVISION {
        @Override public EstadoDenuncia revisar() {
            throw new TransicionInvalidaException("La denuncia ya está en revisión");
        }
        @Override public EstadoDenuncia resolver() { return RESUELTA; }
        @Override public EstadoDenuncia descartar() { return DESCARTADA; }
    },

    RESUELTA {
        @Override public EstadoDenuncia revisar() { throw cerrada(); }
        @Override public EstadoDenuncia resolver() { throw cerrada(); }
        @Override public EstadoDenuncia descartar() { throw cerrada(); }
        @Override public boolean estaCerrada() { return true; }
    },

    DESCARTADA {
        @Override public EstadoDenuncia revisar() { throw cerrada(); }
        @Override public EstadoDenuncia resolver() { throw cerrada(); }
        @Override public EstadoDenuncia descartar() { throw cerrada(); }
        @Override public boolean estaCerrada() { return true; }
    };

    // Cada estado devuelve el estado siguiente o lanza un error si no se puede
    public abstract EstadoDenuncia revisar();
    public abstract EstadoDenuncia resolver();
    public abstract EstadoDenuncia descartar();

    public boolean estaCerrada() {
        return false;
    }

    private static TransicionInvalidaException cerrada() {
        return new TransicionInvalidaException("La denuncia ya está cerrada");
    }
}
