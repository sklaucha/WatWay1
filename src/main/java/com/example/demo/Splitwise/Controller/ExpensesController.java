package com.example.demo.Splitwise.Controller;

import com.example.demo.Model.Entities.ExpensesEntity;
import com.example.demo.Splitwise.Service.SplitwiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpensesController {
    @Autowired
    private SplitwiseService splitwiseService;
    @PostMapping("/sync")
    public List<ExpensesEntity> sync() {
        return splitwiseService.gastosGuardados(); }

}
