package com.example.demo.Model.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "finanzas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinanzasEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", unique = true)
    private UsuarioEntity usuario;

    @Column(nullable = false)
    private Double salarioQuincenal;

    @Column(nullable = false)
    private Double costoHousing;

    @Column(nullable = false)
    private Double costoComida;

    @Column(nullable = false)
    private Double dineroPrevencion;

    @Builder.Default
    @OneToMany(mappedBy = "finanzas", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemFinanzaEntity> items = new ArrayList<>();
}