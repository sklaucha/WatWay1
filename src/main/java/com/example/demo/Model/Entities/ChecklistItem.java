package com.example.demo.Model.Entities;

import com.example.demo.Model.Enums.Documento;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table(name  = "Documentos_Checklist")

public class ChecklistItem {
    @Id
    @GeneratedValue
    private Long id;
@Column(name = "nombre_documento")
    private Documento nombre_documento;

    private boolean completado;
    @ManyToOne
    private Checklist checklist;
}
