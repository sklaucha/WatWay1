package com.example.demo.Model.Excepciones;

// El usuario está logueado pero no puede hacer esa acción
// (ej: editar un tema que no es suyo) -> 403
public class AccionNoPermitidaException extends RuntimeException {
    public AccionNoPermitidaException(String message) {
        super(message);
    }
}
