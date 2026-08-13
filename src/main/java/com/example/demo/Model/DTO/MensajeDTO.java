package com.example.demo.Model.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Setter
@Getter
@AllArgsConstructor
@Builder
public class MensajeDTO {
    private Long id;
    private Long emisorId;
    private String emisorNombre;
    private Long receptorId;
    private String receptorNombre;
    private String contenido;
    private LocalDateTime fechaEnvio;
}