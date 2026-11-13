package com.example.demo.AdminGastos.Service;

import com.example.demo.AdminGastos.DTO.MovimientoDTO;
import com.example.demo.AdminGastos.Entitys.ExpensesEntity;
import com.example.demo.Model.Entities.UsuarioEntity;
import com.example.demo.Model.Enums.ExpenseGroup;
import com.example.demo.AdminGastos.DTO.CategoriaGastoDto;
import com.example.demo.AdminGastos.DTO.GastosDTO;
import com.example.demo.AdminGastos.DTO.GastosResponse;
import com.example.demo.AdminGastos.Mapper.ExpenseMapper;
import com.example.demo.AdminGastos.Repository.ExpensesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    public List<MovimientoDTO> listar(UsuarioEntity usuario) {
        return expensesRepository.findByUsuario_IdOrderByIdDesc(usuario.getId())
                .stream().map(this::toDto).toList();
    }

    public MovimientoDTO crear(MovimientoDTO dto, UsuarioEntity usuario) {
        ExpensesEntity e = ExpensesEntity.builder()
                .description(dto.getDescription())
                .cost(dto.getCost().toPlainString())
                .date(dto.getDate())
                .category(dto.getCategory())
                .type(dto.getType())
                .expenseGroup(grupoDe(dto.getType(), dto.getCategory()))
                .usuario(usuario)
                .build();
        return toDto(expensesRepository.save(e));
    }

    public boolean eliminar(Long id, UsuarioEntity usuario) {
        return expensesRepository.findById(id)
                .filter(e -> e.getUsuario() != null && e.getUsuario().getId().equals(usuario.getId()))
                .map(e -> { expensesRepository.delete(e); return true; })
                .orElse(false);
    }

    private ExpenseGroup grupoDe(String type, String category) {
        if (!"GASTO".equals(type)) return null;
        try {
            return ExpenseGroup.valueOf(category);
        } catch (IllegalArgumentException ex) {
            return ExpenseGroup.OTROS;
        }
    }

    private MovimientoDTO toDto(ExpensesEntity e) {
        MovimientoDTO d = new MovimientoDTO();
        d.setId(e.getId());
        d.setDescription(e.getDescription());
        d.setCost(new BigDecimal(e.getCost()));
        d.setDate(e.getDate());
        d.setCategory(e.getCategory());
        d.setType(e.getType());
        return d;
    }
}