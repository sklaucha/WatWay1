package com.example.demo.Model.Entities;

import com.example.demo.Model.Enums.Mes;
import jakarta.persistence.*;

@Table(name = "Ingresos")
public class IngresosEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "ingreso-mensual")
    private String ingreso_mensual;

    @Column(name = "mes_trabajado")
    private Enum<Mes> mes_Trabajado;
}
