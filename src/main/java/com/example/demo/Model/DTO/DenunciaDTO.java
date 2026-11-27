package com.example.demo.Model.DTO;

import com.example.demo.Model.Enums.EstadoDenuncia;
import com.example.demo.Model.Enums.MotivoDenuncia;
import com.example.demo.Model.Enums.TipoDenuncia;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DenunciaDTO {
    private Long id;
    private TipoDenuncia tipo;
    private Long referenciaId;
    private MotivoDenuncia motivo;
    private String descripcion;
    private String contenidoDenunciado;
    private EstadoDenuncia estado;
    private String denuncianteNombre;
    private Long denunciadoId;
    private String denunciadoNombre;
    private boolean denunciadoActivo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaCierre;
    private String notaAdmin;
    private String medidas;
    private String revisadaPorNombre;
}
