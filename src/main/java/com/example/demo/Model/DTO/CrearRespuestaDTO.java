package com.example.demo.Model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Se usa para crear y para editar una respuesta
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearRespuestaDTO {

    @NotBlank(message = "La respuesta no puede estar vacía")
    @Size(max = 2000, message = "La respuesta no puede superar los 2000 caracteres")
    private String contenido;
}
