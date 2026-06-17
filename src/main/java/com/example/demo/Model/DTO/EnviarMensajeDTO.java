package com.example.demo.Model.DTO;

import lombok.Data;

@Data
public class EnviarMensajeDTO {
    private Long receptorId;
    private String contenido;
}