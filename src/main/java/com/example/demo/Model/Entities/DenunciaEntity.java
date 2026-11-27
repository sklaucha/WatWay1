package com.example.demo.Model.Entities;

import com.example.demo.Model.Enums.EstadoDenuncia;
import com.example.demo.Model.Enums.MotivoDenuncia;
import com.example.demo.Model.Enums.TipoDenuncia;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "denuncia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DenunciaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Quién denuncia
    @ManyToOne
    @JoinColumn(name = "denunciante_id", nullable = false)
    private UsuarioEntity denunciante;

    // A quién se denuncia (el autor del contenido)
    @ManyToOne
    @JoinColumn(name = "denunciado_id", nullable = false)
    private UsuarioEntity denunciado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoDenuncia tipo;

    // Id de lo denunciado: mensaje, tema, respuesta o usuario
    @Column(name = "referencia_id", nullable = false)
    private Long referenciaId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MotivoDenuncia motivo;

    @Column(length = 500)
    private String descripcion;

    // Copia del contenido al momento de denunciar: el admin lo ve
    // aunque después el autor lo edite o lo borre
    @Column(name = "contenido_denunciado", length = 2200)
    private String contenidoDenunciado;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoDenuncia estado = EstadoDenuncia.PENDIENTE;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaCierre;

    @Column(name = "nota_admin", length = 500)
    private String notaAdmin;

    // Qué hizo el admin al resolver (ej: "Contenido borrado, usuario desactivado")
    @Column(name = "medidas", length = 200)
    private String medidas;

    @ManyToOne
    @JoinColumn(name = "revisada_por_id")
    private UsuarioEntity revisadaPor;

    @PrePersist
    public void prePersist() {
        fechaCreacion = LocalDateTime.now();
    }

    // ---------- STATE: la denuncia delega en su estado ----------

    public void revisar(UsuarioEntity admin) {
        this.estado = estado.revisar();
        this.revisadaPor = admin;
    }

    public void resolver(UsuarioEntity admin, String nota, String medidas) {
        this.estado = estado.resolver();
        cerrar(admin, nota);
        this.medidas = medidas;
    }

    public void descartar(UsuarioEntity admin, String nota) {
        this.estado = estado.descartar();
        cerrar(admin, nota);
    }

    private void cerrar(UsuarioEntity admin, String nota) {
        this.revisadaPor = admin;
        this.notaAdmin = (nota == null || nota.isBlank()) ? null : nota.trim();
        this.fechaCierre = LocalDateTime.now();
    }
}
