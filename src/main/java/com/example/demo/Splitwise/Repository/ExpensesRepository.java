package com.example.demo.Splitwise.Repository;

import com.example.demo.Model.Entities.ExpensesEntity;
import com.example.demo.Model.Enums.ExpenseGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpensesRepository extends JpaRepository<ExpensesEntity,Long> {
    List<ExpensesEntity> findByExpenseGroup(ExpenseGroup expenseGroup);
}
