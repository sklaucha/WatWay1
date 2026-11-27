package com.example.demo.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvisoDTO {
    private Long id;
    private String titulo;
    private String mensaje;
    private boolean importante;
    private boolean activo;
    private LocalDateTime fechaCreacion;
    private String autorNombre;
}
