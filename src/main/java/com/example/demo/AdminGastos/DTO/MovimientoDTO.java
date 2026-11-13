package com.example.demo.AdminGastos.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class MovimientoDTO {
    private Long id;

    @NotBlank
    private String description;

    @NotNull
    @DecimalMin("0")
    @DecimalMax("10000000")
    private BigDecimal cost;

    @NotBlank
    private String date;

    @NotBlank
    private String category;

    @NotBlank
    @Pattern(regexp = "GASTO|INGRESO")
    private String type;
}