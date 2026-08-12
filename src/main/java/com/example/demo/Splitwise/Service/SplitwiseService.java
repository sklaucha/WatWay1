package com.example.demo.Splitwise.Service;

import com.example.demo.Model.Entities.ExpensesEntity;
import com.example.demo.Model.Enums.ExpenseGroup;
import com.example.demo.Splitwise.DTO.SplitwiseExpensesResponse;
import com.example.demo.Splitwise.Mapper.ExpenseMapper;
import com.example.demo.Splitwise.Repository.ExpensesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class SplitwiseService {
    @Autowired
    private  RestClient splitwiseRestClient;

    @Autowired
    private ExpensesRepository expensesRepository;

    public List<ExpensesEntity > gastosGuardados() {

        SplitwiseExpensesResponse response = splitwiseRestClient.get()
                .uri("/get_expenses").retrieve()
                .body(SplitwiseExpensesResponse.class);


         List<ExpensesEntity> entities = response.getGastos().stream()
                 .map(ExpenseMapper::toEntity) .toList();
         return expensesRepository.saveAll(entities); }


    public List<ExpensesEntity> getByGroup(ExpenseGroup tipo_gasto) {
        return expensesRepository.findByExpenseGroup(tipo_gasto); }

    }


