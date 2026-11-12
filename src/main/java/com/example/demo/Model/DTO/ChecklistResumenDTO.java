package com.example.demo.Model.DTO;

import lombok.*;

import java.util.List;

@Data
@Setter
@Getter
@AllArgsConstructor
@Builder
public class ChecklistResumenDTO {
    private Long checklistId;
    private double progreso;
    private List<ChecklistItemDTO> completados;
    private List<ChecklistItemDTO> faltantes;
}
