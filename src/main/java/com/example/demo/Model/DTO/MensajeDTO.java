package com.example.demo.Model.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MensajeDTO {
    private Long id;
    private Long emisorId;
    private String emisorNombre;
    private Long receptorId;
    private String receptorNombre;
    // null si el mensaje fue eliminado
    private String contenido;
    private LocalDateTime fechaEnvio;

    private boolean editado;
    private LocalDateTime fechaEdicion;
    private boolean leido;
    private LocalDateTime fechaLectura;
    private boolean eliminado;
    // true solo para mis mensajes que el otro todavía no leyó
    private boolean puedeModificar;
}
