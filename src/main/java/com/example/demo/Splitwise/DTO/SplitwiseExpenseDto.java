package com.example.demo.Splitwise.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter@Setter
@AllArgsConstructor

public class SplitwiseExpenseDto {

    private Long id;
    private String description;
    private String cost;
    private String date;
    private SplitwiseCategoryDto category;

}
