package com.example.demo.Model.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Builder
@Getter
@Setter
@Table(name = "checklist")
    public class Checklist {

        @Id
        @GeneratedValue
        private Long id;

        @NotNull(message = "El checklist debe estar asociado a una oferta de trabajo")
        @OneToOne(optional = false)
        @JoinColumn(name = "job_info_id", nullable = false)
        private JobOfferInfo trabajo;

        @NotNull(message = "El checklist debe tener un usuario asociado")
        @OneToOne(optional = false)
        @JoinColumn(name = "usuario_id", nullable = false)
        private UsuarioEntity usuario;

        @OneToMany(mappedBy = "checklist", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<ChecklistItem> items;

}
