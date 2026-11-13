package com.example.demo.AdminGastos.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MovimientoDTO {
    private Long id;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
    private String description;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0", message = "El monto no puede ser negativo")
    @DecimalMax(value = "10000000", message = "El monto es demasiado alto")
    @Digits(integer = 8, fraction = 2, message = "El monto puede tener hasta 2 decimales")
    private BigDecimal cost;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate date; // llega del front como "2026-11-12"

    @NotBlank(message = "La categoría es obligatoria")
    @Size(max = 50, message = "La categoría no puede superar los 50 caracteres")
    private String category;

    @NotBlank(message = "El tipo es obligatorio")
    @Pattern(regexp = "GASTO|INGRESO", message = "El tipo debe ser GASTO o INGRESO")
    private String type;
}
