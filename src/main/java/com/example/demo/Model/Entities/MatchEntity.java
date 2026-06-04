package com.example.demo.Model.Entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "matches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_origen_id", nullable = false)
    private UsuarioEntity usuarioOrigen;

    @ManyToOne
    @JoinColumn(name = "usuario_destino_id", nullable = false)
    private UsuarioEntity usuarioDestino;

    @Column(nullable = false)
    private boolean liked;

    @Column(nullable = false)
    private boolean matched;
}