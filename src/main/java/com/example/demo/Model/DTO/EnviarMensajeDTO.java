package com.example.demo.Model.DTO;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@Builder
public class EnviarMensajeDTO {
    private Long receptorId;
    private String contenido;
}