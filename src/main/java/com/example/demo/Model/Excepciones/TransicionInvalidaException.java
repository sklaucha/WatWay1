package com.example.demo.Model.Excepciones;

// Se intentó un cambio de estado que no corresponde (ej: resolver una denuncia ya cerrada) -> 409
public class TransicionInvalidaException extends RuntimeException {
    public TransicionInvalidaException(String message) {
        super(message);
    }
}
