package com.example.demo.Model.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// El admin marca o desmarca si la agencia tiene empleadores en un estado
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoresDTO {

    @NotNull(message = "Indicá si el estado tiene empleadores")
    private Boolean tieneEmpleadores;
}
