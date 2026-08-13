package com.example.demo.AdminGastos.Service;

import com.example.demo.AdminGastos.Entitys.ExpensesEntity;
import com.example.demo.Model.Enums.ExpenseGroup;
import com.example.demo.AdminGastos.DTO.CategoriaGastoDto;
import com.example.demo.AdminGastos.DTO.GastosDTO;
import com.example.demo.AdminGastos.DTO.GastosResponse;
import com.example.demo.AdminGastos.Mapper.ExpenseMapper;
import com.example.demo.AdminGastos.Repository.ExpensesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@AllArgsConstructor
@Service
public class ExpensesService {
    private ExpensesRepository expensesRepository;

    public List<ExpensesEntity> gastosGuardados() {
        GastosResponse batch = getMockData();

        List<ExpensesEntity> entities = batch.getGastos().stream()
                .map(ExpenseMapper::toEntity).toList();
        return expensesRepository.saveAll(entities);
    }

    private GastosResponse getMockData() {
        CategoriaGastoDto categoriaComida = new CategoriaGastoDto(1L, "Groceries");
        CategoriaGastoDto categoriaTransporte = new CategoriaGastoDto(2L, "Transportation");
        CategoriaGastoDto categoriaCasa = new CategoriaGastoDto(3L, "Rent");

        GastosDTO gasto1 = new GastosDTO(101L, "Supermercado semanal", "45.50", "2026-08-10", categoriaComida);
        GastosDTO gasto2 = new GastosDTO(102L, "Uber al aeropuerto", "22.00", "2026-08-11", categoriaTransporte);
        GastosDTO gasto3 = new GastosDTO(103L, "Alquiler mensual", "600.00", "2026-08-01", categoriaCasa);

        GastosResponse batch = new GastosResponse();
        batch.setGastos(List.of(gasto1, gasto2, gasto3));
        return batch;
    }

    public List<ExpensesEntity> getByGroup(ExpenseGroup tipo_gasto) {
        return expensesRepository.findByExpenseGroup(tipo_gasto);
    }
}