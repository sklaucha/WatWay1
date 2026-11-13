package com.example.demo.AdminGastos.Entitys;
import com.example.demo.Model.Entities.UsuarioEntity;
import com.example.demo.Model.Enums.ExpenseGroup;
import jakarta.persistence.*;
import lombok.*;
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

    @Column(name = "costo")
    private String cost;

    @Column(name = "fecha")
    private String date;

    @Column(name = "categoria")
    private String category;

    @Enumerated(EnumType.STRING)
    @Column(name = "grupo")
    private ExpenseGroup expenseGroup;

    @Column(name = "tipo")
    private String type; // "GASTO" o "INGRESO"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @ToString.Exclude
    private UsuarioEntity usuario;
}