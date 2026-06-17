package com.example.demo.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinanzasResponseDTO {
    private Double salarioQuincenal;
    private Double costoHousing;
    private Double costoComida;
    private Double dineroPrevencion;
    private List<ItemFinanzaDTO> items;

    // Calculados
    private Double totalIngresos;
    private Double totalGastos;
    private Double balance;
}