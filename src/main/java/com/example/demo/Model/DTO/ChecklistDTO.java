package com.example.demo.Model.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@Builder
public class ChecklistDTO {
    private Long id;

    @NotNull(message = "El id del trabajo es obligatorio")
    private Long trabajoId;

    @NotNull(message = "El id del usuario es obligatorio")
    private Long usuarioId;
}
