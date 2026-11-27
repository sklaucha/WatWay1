package com.example.demo.Model.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// Aviso general que el admin publica para todos los usuarios
@Entity
@Table(name = "aviso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvisoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(nullable = false, length = 500)
    private String mensaje;

    // Los importantes se muestran resaltados
    @Builder.Default
    @Column(nullable = false)
    private boolean importante = false;

    // Solo los activos les aparecen a los usuarios
    @Builder.Default
    @Column(nullable = false)
    private boolean activo = true;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = false)
    private UsuarioEntity autor;

    @PrePersist
    public void prePersist() {
        fechaCreacion = LocalDateTime.now();
    }

    public void editar(String titulo, String mensaje, boolean importante) {
        this.titulo = titulo.trim();
        this.mensaje = mensaje.trim();
        this.importante = importante;
    }
}
