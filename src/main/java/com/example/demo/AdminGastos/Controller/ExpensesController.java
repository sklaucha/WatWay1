package com.example.demo.AdminGastos.Controller;

import com.example.demo.AdminGastos.Entitys.ExpensesEntity;
import com.example.demo.AdminGastos.Service.ExpensesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpensesController {
    @Autowired
    private ExpensesService expensesService;

    @PostMapping("/sync")
    public List<ExpensesEntity> sync() {
        return expensesService.gastosGuardados();
    }
}
