package com.example.demo.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespuestaDTO {
    private Long id;
    private String contenido;
    private String autorNombre;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaEdicion;
    // Para que el front sepa qué botones mostrar
    private boolean puedeEditar;
    private boolean puedeBorrar;
}
