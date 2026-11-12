package com.example.demo.AdminGastos.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter@Setter
@AllArgsConstructor

public class GastosDTO {

    private Long id;
    private String description;
    private String cost;
    private String date;
    private CategoriaGastoDto category;

}
