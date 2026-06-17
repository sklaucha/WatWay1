package com.example.demo.Model.DTO;

import com.example.demo.Model.Enums.TipoItem;
import lombok.Data;

@Data
public class ItemFinanzaDTO {
    private String descripcion;
    private Double monto;
    private TipoItem tipo;
}