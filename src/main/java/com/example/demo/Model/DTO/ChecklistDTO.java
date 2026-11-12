package com.example.demo.Model.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor // Jackson lo necesita para armar el objeto desde el JSON
@AllArgsConstructor
@Builder
public class ChecklistDTO {
    private Long id;

    @NotNull(message = "El id del trabajo es obligatorio")
    private Long trabajoId;

    @NotNull(message = "El id del usuario es obligatorio")
    private Long usuarioId;
}
