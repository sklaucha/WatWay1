package com.example.demo.Model.DTO;

import com.example.demo.Model.Enums.TipoTema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

// Un tema completo con sus respuestas
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemaDetalleDTO {
    private Long id;
    private String titulo;
    private String contenido;
    private TipoTema tipo;
    private String estadoCodigo;   // null = foro global
    private String estadoNombre;
    private String autorNombre;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaEdicion;
    private boolean puedeEditar;
    private boolean puedeBorrar;
    private List<RespuestaDTO> respuestas;
}
