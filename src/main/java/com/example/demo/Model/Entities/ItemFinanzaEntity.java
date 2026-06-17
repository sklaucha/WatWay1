package com.example.demo.Model.Entities;

import com.example.demo.Model.Enums.TipoItem;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "items_finanza")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemFinanzaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "finanzas_id", nullable = false)
    private FinanzasEntity finanzas;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private Double monto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoItem tipo;
}