package com.example.demo.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Cuántos mensajes sin leer tengo de cada usuario
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoLeidosDTO {
    private Long usuarioId;
    private long cantidad;
}
