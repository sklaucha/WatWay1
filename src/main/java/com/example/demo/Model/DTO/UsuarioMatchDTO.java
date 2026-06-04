package com.example.demo.Model.DTO;

import com.example.demo.Model.Enums.Sexo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioMatchDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private Sexo sexo;
    private String posicionLaboral;
    private String ciudadDestino;
    private String descripcion;
}