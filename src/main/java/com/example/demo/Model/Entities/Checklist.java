package com.example.demo.Model.Entities;

import jakarta.persistence.*;

import java.util.List;
@Table(name = "checklist")
public class Checklist {
    @Id
    @GeneratedValue
    private Long id;

   @Column(name = "job_info") @OneToOne
    private JobOfferInfo trabajo;

   @Column(name = "usuario")
    private UsuarioEntity usuario;

   @Column(name = "items")
    @OneToMany(mappedBy = "checklist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChecklistItem> items;

}
