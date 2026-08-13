package com.example.demo.Model.DTO;

import com.example.demo.Model.Enums.Sexo;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@Builder
public class RegisterRequestDTO {

    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String posicionLaboral;
    private String ciudadDestino;
    private Sexo sexo;
    private String descripcion;
}