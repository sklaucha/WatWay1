package com.example.demo.AdminGastos.Mapper;

import com.example.demo.AdminGastos.Entitys.ExpensesEntity;
import com.example.demo.AdminGastos.DTO.GastosDTO;

public class ExpenseMapper {
    public static ExpensesEntity toEntity(GastosDTO dto) {
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
