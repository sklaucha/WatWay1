package com.example.demo.Model.DTO;

import com.example.demo.Model.Enums.Sexo;
import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor // Jackson lo necesita para armar el objeto desde el JSON
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