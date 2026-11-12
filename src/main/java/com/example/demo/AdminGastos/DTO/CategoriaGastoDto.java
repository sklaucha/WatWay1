package com.example.demo.AdminGastos.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
@AllArgsConstructor
public class CategoriaGastoDto {
    private Long id;
    private String name;
}
