package com.example.demo.Model.Entities;

import com.example.demo.Model.Enums.Documento;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "documento", nullable = false)
    private Documento documento;

    @Column(name = "completado")
    private boolean completado;

    @Column(name = "fecha_limite")
    private LocalDate fechaLimite;

    @ManyToOne
    @JoinColumn(name = "checklist_id")
    private Checklist checklist;
}
