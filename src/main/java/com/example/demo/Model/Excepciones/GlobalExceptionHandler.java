package com.example.demo.Model.Exceptions;

import com.example.demo.Model.Excepciones.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUsuarioNotFound(UsuarioNotFoundException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailYaRegistradoException.class)
    public ResponseEntity<Map<String, Object>> handleEmailYaRegistrado(EmailYaRegistradoException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UsuarioYaEvaluadoException.class)
    public ResponseEntity<Map<String, Object>> handleUsuarioYaEvaluado(UsuarioYaEvaluadoException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RolNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleRolNotFound(RolNotFoundException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TokenInvalidoException.class)
    public ResponseEntity<Map<String, Object>> handleTokenInvalido(TokenInvalidoException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(com.example.demo.Model.Exceptions.SinCandidatosException.class)
    public ResponseEntity<Map<String, Object>> handleSinCandidatos(com.example.demo.Model.Exceptions.SinCandidatosException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFound(EntityNotFoundException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OfferNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleOfferNotFound(OfferNotFoundException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Falla un @Valid (ej: nombre vacío) -> 400 con el primer mensaje de error.
    // Sin esto Spring redirige a /error, que está protegido, y responde 401.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidacion(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(e -> e.getDefaultMessage())
                .orElse("Datos inválidos");
        return buildResponse(mensaje, HttpStatus.BAD_REQUEST);
    }

    // Datos inválidos enviados por el cliente (ej: falta el documento) -> 400
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DocumentoYaAgregadoException.class)
    public ResponseEntity<Map<String, Object>> handleDocumentoYaAgregado(DocumentoYaAgregadoException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(OfertaExistenteException.class)
    public ResponseEntity<Map<String, Object>> handleOfertaExistente(OfertaExistenteException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return buildResponse("Parámetro inválido: " + ex.getName(), HttpStatus.BAD_REQUEST);
    }

    // Login fallido (contraseña incorrecta, usuario inexistente o desactivado):
    // es un error del cliente, así que va 401 y no 500.
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthentication(AuthenticationException ex) {
        return buildResponse("Email o contraseña incorrectos", HttpStatus.UNAUTHORIZED);
    }

    // JSON roto, texto donde va un número, fecha imposible (2026-02-30),
    // valor de enum que no existe o body vacío -> error del cliente, 400
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleJsonInvalido(HttpMessageNotReadableException ex) {
        return buildResponse("Formato de datos inválido. Revisá los campos enviados.", HttpStatus.BAD_REQUEST);
    }

    // Red de seguridad: la base rechaza el dato (texto demasiado largo, campo obligatorio vacío) -> 400
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDatosInvalidos(DataIntegrityViolationException ex) {
        return buildResponse("Los datos enviados no son válidos.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        // El detalle va solo a la consola: al cliente no le mostramos
        // mensajes internos (SQL, nombres de clases, etc.)
        ex.printStackTrace();
        return buildResponse("Ocurrió un error inesperado en el servidor", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Map<String, Object>> buildResponse(String message, HttpStatus status) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", message);
        body.put("status", status.value());
        body.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(status).body(body);
    }
}