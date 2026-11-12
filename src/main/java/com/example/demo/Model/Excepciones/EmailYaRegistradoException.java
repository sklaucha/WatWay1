package com.example.demo.Model.Excepciones;

public class EmailYaRegistradoException extends RuntimeException {
    public EmailYaRegistradoException(String message) {
        super(message);
    }
}