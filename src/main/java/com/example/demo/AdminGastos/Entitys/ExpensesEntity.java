package com.example.demo.AdminGastos.Entitys;

import com.example.demo.Model.Entities.UsuarioEntity;
import com.example.demo.Model.Enums.ExpenseGroup;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "Gastos")
public class ExpensesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descripcion")
    private String description;

    // DECIMAL(12,2): plata con 2 decimales, sin errores de redondeo
    @Column(name = "costo", precision = 12, scale = 2)
    private BigDecimal cost;

    @Column(name = "fecha")
    private LocalDate date;

    @Column(name = "categoria")
    private String category;

    // Solo para gastos: COMIDA, TRANSPORTE, CASA u OTROS
    @Enumerated(EnumType.STRING)
    @Column(name = "grupo")
    private ExpenseGroup expenseGroup;

    @Column(name = "tipo")
    private String type; // "GASTO" o "INGRESO"

    // Cada movimiento pertenece a un usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @ToString.Exclude
    private UsuarioEntity usuario;
}
