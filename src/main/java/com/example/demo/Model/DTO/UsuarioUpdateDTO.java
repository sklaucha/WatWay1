package com.example.demo.Model.DTO;

import com.example.demo.Model.Enums.Sexo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioUpdateDTO {

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @NotBlank
    private String posicionLaboral;

    @NotBlank
    private String ciudadDestino;

    @NotNull
    private Sexo sexo;

    @Size(max = 500)
    private String descripcion;
}
