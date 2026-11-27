package com.example.demo.Model.Entities;

import com.example.demo.Model.Enums.TipoTema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "foro_tema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForoTemaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, length = 2000)
    private String contenido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TipoTema tipo;

    // null = el tema es del foro global
    @ManyToOne
    @JoinColumn(name = "estado_codigo")
    private EstadoUsaEntity estado;

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
    // El tema tiene los datos (autor y tipo), así que él responde estas preguntas.

    public boolean esAutor(UsuarioEntity usuario) {
        return autor.getId().equals(usuario.getId());
    }

    // La regla depende del tipo (POLIMORFISMO en TipoTema)
    public boolean puedeEditarlo(UsuarioEntity usuario, boolean esAdmin) {
        return tipo.puedeEditar(esAutor(usuario), esAdmin);
    }

    // Borrar: su autor o un admin (moderación)
    public boolean puedeBorrarlo(UsuarioEntity usuario, boolean esAdmin) {
        return esAutor(usuario) || esAdmin;
    }

    public boolean admiteRespuestas() {
        return tipo.admiteRespuestas();
    }

    public void editar(String nuevoTitulo, String nuevoContenido) {
        this.titulo = nuevoTitulo.trim();
        this.contenido = nuevoContenido.trim();
        this.fechaEdicion = LocalDateTime.now();
    }
}
