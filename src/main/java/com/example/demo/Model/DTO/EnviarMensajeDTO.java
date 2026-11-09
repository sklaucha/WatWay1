package com.example.demo.Model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnviarMensajeDTO {

    @NotNull(message = "El receptor es obligatorio")
    private Long receptorId;

    @NotBlank(message = "El mensaje no puede estar vacío")
    @Size(max = 500, message = "El mensaje no puede superar los 500 caracteres")
    private String contenido;
}