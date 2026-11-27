package com.example.demo.Model.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "foro_respuesta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForoRespuestaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2000)
    private String contenido;

    @ManyToOne
    @JoinColumn(name = "tema_id", nullable = false)
    private ForoTemaEntity tema;

    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = false)
    private UsuarioEntity autor;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_edicion")
    private LocalDateTime fechaEdicion;

    @PrePersist
    public void prePersist() {
        fechaCreacion = LocalDateTime.now();
    }

    // ---------- EXPERTO EN INFORMACIÓN (GRASP) ----------

    public boolean esAutor(UsuarioEntity usuario) {
        return autor.getId().equals(usuario.getId());
    }

    // Editar: solo su autor
    public boolean puedeEditarla(UsuarioEntity usuario) {
        return esAutor(usuario);
    }

    // Borrar: su autor o un admin (moderación)
    public boolean puedeBorrarla(UsuarioEntity usuario, boolean esAdmin) {
        return esAutor(usuario) || esAdmin;
    }

    public void editar(String nuevoContenido) {
        this.contenido = nuevoContenido.trim();
        this.fechaEdicion = LocalDateTime.now();
    }
}
