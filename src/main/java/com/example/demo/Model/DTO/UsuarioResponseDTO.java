package com.example.demo.Model.DTO;

import com.example.demo.Model.Enums.Sexo;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@Builder
public class UsuarioResponseDTO {

    private Long id;

    private String nombre;

    private String apellido;

    private String email;

    private String posicionLaboral;

    private String ciudadDestino;

    private Sexo sexo;

    private String descripcion;
}