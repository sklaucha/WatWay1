package com.example.demo.AdminGastos.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MovimientoDTO {
    private Long id;

    @NotBlank(message = "La descripción es obligatoria")
    private String description;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0", message = "El monto no puede ser negativo")
    @DecimalMax(value = "10000000", message = "El monto es demasiado alto")
    private BigDecimal cost;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate date; // llega del front como "2026-11-12"

    @NotBlank(message = "La categoría es obligatoria")
    private String category;

    @NotBlank(message = "El tipo es obligatorio")
    @Pattern(regexp = "GASTO|INGRESO", message = "El tipo debe ser GASTO o INGRESO")
    private String type;
}
