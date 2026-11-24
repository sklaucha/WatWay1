package com.example.demo.Model.DTO;

import com.example.demo.Model.Enums.TipoTema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// Un tema en un listado (sin el contenido completo ni las respuestas)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemaResumenDTO {
    private Long id;
    private String titulo;
    private TipoTema tipo;
    private String autorNombre;
    private LocalDateTime fechaCreacion;
    private long cantidadRespuestas;
}
