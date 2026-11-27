package com.example.demo.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Lo que necesita el mapa por cada estado
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoForoDTO {
    private String codigo;
    private String nombre;
    private String region;
    private boolean tieneEmpleadores;
    private long cantidadTemas;
}
