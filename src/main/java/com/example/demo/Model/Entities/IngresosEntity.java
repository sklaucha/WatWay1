package com.example.demo.Model.Entities;

import com.example.demo.Model.Enums.Mes;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "Ingresos")
public class IngresosEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
    @Column(name = "ingreso-mensual")
    private String ingreso_mensual;

    @Column(name = "mes_trabajado")
    private Enum<Mes> mes_Trabajado;
}
