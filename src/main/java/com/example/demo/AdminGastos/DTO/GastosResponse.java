package com.example.demo.AdminGastos.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Data
@Getter
@Setter
public class GastosResponse {
    private List<GastosDTO> gastos;
}
