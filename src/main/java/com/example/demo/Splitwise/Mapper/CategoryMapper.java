package com.example.demo.Splitwise.Mapper;

import com.example.demo.Model.Enums.ExpenseGroup;

import java.util.Map;
import java.util.Set;

public class CategoryMapper {
    private static final Map<ExpenseGroup, Set<String>> GROUPS = Map.of(
            ExpenseGroup.COMIDA, Set.of("Groceries", "Dining out", "Fast food", "Alcohol/bars"),
            ExpenseGroup.TRANSPORTE, Set.of("Gas/fuel", "Bus/train", "Taxi", "Parking", "Car"),
            ExpenseGroup.CASA, Set.of("Rent", "Mortgage", "Electricity", "Water", "Heat/gas", "Internet")
    );

    public static ExpenseGroup mapCategory(String splitwiseCategory) {
        return GROUPS.entrySet().stream()
                .filter(e -> e.getValue().contains(splitwiseCategory))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(ExpenseGroup.OTROS);
    }
}
