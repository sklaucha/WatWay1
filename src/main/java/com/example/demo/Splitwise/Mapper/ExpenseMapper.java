package com.example.demo.Splitwise.Mapper;

import com.example.demo.Model.Entities.ExpensesEntity;
import com.example.demo.Splitwise.DTO.SplitwiseExpenseDto;

public class ExpenseMapper {
    public static ExpensesEntity toEntity(SplitwiseExpenseDto dto) {
        return ExpensesEntity.builder()
                .id(dto.getId())
                .description(dto.getDescription())
                .cost(dto.getCost())
                .date(dto.getDate())
                .category(dto.getCategory().getName())
                .expenseGroup(CategoryMapper.mapCategory(dto.getCategory().getName()))
                .build();
    }
}
