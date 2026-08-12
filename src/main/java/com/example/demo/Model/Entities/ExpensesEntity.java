package com.example.demo.Model.Entities;
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
@Table(name  = "Gastos")
public class ExpensesEntity {
    @Id
    private Long id; // mismo id que te da Splitwise

    @Column(name = "descripcion")
    private String description;

    @Column(name = "costo")
    private String cost;

    @Column(name = "fecha")
    private String date;

    @Column(name = "categoria")
    private String category; // categoría original de Splitwise

    @Enumerated(EnumType.STRING)
    @Column(name = "grupo")
    private ExpenseGroup expenseGroup; // tu grupo propio
}
