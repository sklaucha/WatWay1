package com.example.demo.Excepciones;

public class UsuarioYaEvaluadoException extends RuntimeException {
    public UsuarioYaEvaluadoException(String message) {
        super(message);
    }
}