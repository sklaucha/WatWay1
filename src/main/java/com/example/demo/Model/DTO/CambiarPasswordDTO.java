package com.example.demo.Model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CambiarPasswordDTO {

    @NotBlank(message = "Ingresá tu contraseña actual")
    private String actual;

    @NotBlank(message = "Ingresá la nueva contraseña")
    @Size(min = 6, max = 72, message = "La nueva contraseña debe tener entre 6 y 72 caracteres")
    private String nueva;
}
