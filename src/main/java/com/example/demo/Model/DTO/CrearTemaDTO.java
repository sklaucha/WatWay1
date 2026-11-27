package com.example.demo.Model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Se usa para crear y para editar un tema (o una FAQ)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearTemaDTO {

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 150, message = "El título no puede superar los 150 caracteres")
    private String titulo;

    @NotBlank(message = "El contenido es obligatorio")
    @Size(max = 2000, message = "El contenido no puede superar los 2000 caracteres")
    private String contenido;
}
