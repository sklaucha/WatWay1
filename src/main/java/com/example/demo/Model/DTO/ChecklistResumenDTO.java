package com.example.demo.Model.DTO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Data
public class ChecklistResumenDTO {
    private Long checklistId;
    private double progreso;
    private List<ChecklistItemDTO> completados;
    private List<ChecklistItemDTO> faltantes;
}
