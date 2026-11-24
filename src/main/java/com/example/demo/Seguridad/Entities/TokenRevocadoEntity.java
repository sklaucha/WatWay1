package com.example.demo.Seguridad.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// Un token que se cerró con logout o baja de cuenta y ya no se puede usar
@Entity
@Table(name = "token_revocado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenRevocadoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Hash SHA-256 del token (unique crea un índice: la búsqueda es rápida)
    @Column(name = "token_hash", nullable = false, unique = true, length = 64)
    private String tokenHash;

    // Cuándo vence el token: después de esa fecha se puede borrar la fila
    @Column(nullable = false)
    private LocalDateTime expiracion;
}
