package com.example.demo.Model.Entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mensajes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MensajeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "emisor_id", nullable = false)
    private UsuarioEntity emisor;

    @ManyToOne
    @JoinColumn(name = "receptor_id", nullable = false)
    private UsuarioEntity receptor;

    @Column(nullable = false)
    private String contenido;

    @Column(nullable = false)
    private LocalDateTime fechaEnvio;

    @PrePersist
    public void prePersist() {
        fechaEnvio = LocalDateTime.now();
    }
}