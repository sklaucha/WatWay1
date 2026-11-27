package com.example.demo.Model.DTO;

import com.example.demo.Model.Enums.MotivoDenuncia;
import com.example.demo.Model.Enums.TipoDenuncia;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearDenunciaDTO {

    @NotNull(message = "Indicá qué querés denunciar")
    private TipoDenuncia tipo;

    // Id del mensaje, tema, respuesta o usuario denunciado
    @NotNull(message = "Falta el id de lo que querés denunciar")
    private Long referenciaId;

    @NotNull(message = "Elegí un motivo")
    private MotivoDenuncia motivo;

    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    private String descripcion;
}
