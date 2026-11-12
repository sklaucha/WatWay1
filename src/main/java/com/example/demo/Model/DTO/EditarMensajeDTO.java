package com.example.demo.Model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Para editar un mensaje solo se cambia el texto
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditarMensajeDTO {

    @NotBlank(message = "El mensaje no puede estar vacío")
    @Size(max = 500, message = "El mensaje no puede superar los 500 caracteres")
    private String contenido;
}
