package com.example.demo.Model.DTO;

import com.example.demo.Model.Enums.Documento;
import lombok.*;

import java.time.LocalDate;
@Data
@Getter
@Setter
@AllArgsConstructor
@Builder
public class ChecklistItemDTO {
    private Long id;
    private Documento documento;
    private boolean completado;
    private LocalDate fechaLimite;
}
