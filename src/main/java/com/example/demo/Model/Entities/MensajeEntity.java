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

    @Column(nullable = false, length = 500)
    private String contenido;

    @Column(nullable = false)
    private LocalDateTime fechaEnvio;

    // null = nunca se editó
    private LocalDateTime fechaEdicion;

    // El receptor abrió el chat y lo vio
    @Builder.Default
    @Column(nullable = false)
    private boolean leido = false;

    private LocalDateTime fechaLectura;

    // Borrado lógico: la fila queda para mostrar "Mensaje eliminado"
    @Builder.Default
    @Column(nullable = false)
    private boolean eliminado = false;

    @PrePersist
    public void prePersist() {
        fechaEnvio = LocalDateTime.now();
    }

    // ---------- EXPERTO EN INFORMACIÓN (GRASP) ----------
    // El mensaje sabe quién lo mandó y si ya fue leído o borrado,
    // así que él decide si se puede modificar.

    public boolean esEmisor(UsuarioEntity usuario) {
        return emisor.getId().equals(usuario.getId());
    }

    // Solo quien lo mandó, y solo mientras el otro no lo haya leído
    public boolean puedeModificar(UsuarioEntity usuario) {
        return esEmisor(usuario) && !leido && !eliminado;
    }

    public void editar(String nuevoContenido) {
        this.contenido = nuevoContenido.trim();
        this.fechaEdicion = LocalDateTime.now();
    }

    // Se vacía el texto: lo borrado no queda guardado en la base
    public void eliminar() {
        this.eliminado = true;
        this.contenido = "";
    }

    public void marcarLeido() {
        if (!leido) {
            this.leido = true;
            this.fechaLectura = LocalDateTime.now();
        }
    }
}
