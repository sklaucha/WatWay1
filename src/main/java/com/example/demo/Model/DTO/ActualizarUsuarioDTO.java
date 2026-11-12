package com.example.demo.Model.DTO;

import com.example.demo.Model.Enums.Sexo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Datos que el usuario puede editar de su perfil.
// El email y la contraseña no van acá: el email es el "usuario" del login.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarUsuarioDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 60, message = "El nombre no puede superar los 60 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 60, message = "El apellido no puede superar los 60 caracteres")
    private String apellido;

    @NotBlank(message = "La posición laboral es obligatoria")
    @Size(max = 100)
    private String posicionLaboral;

    @NotBlank(message = "La ciudad destino es obligatoria")
    @Size(max = 100)
    private String ciudadDestino;

    @NotNull(message = "El sexo es obligatorio")
    private Sexo sexo;

    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    private String descripcion;
}
