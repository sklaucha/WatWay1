package com.example.demo.Model.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Para prender o apagar algo (por ejemplo, un aviso)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivoDTO {

    @NotNull(message = "Indicá si está activo")
    private Boolean activo;
}
