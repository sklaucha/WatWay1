package com.example.demo.AdminGastos.Repository;

import com.example.demo.AdminGastos.Entitys.ExpensesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpensesRepository extends JpaRepository<ExpensesEntity, Long> {
    // Movimientos de un usuario, del más reciente al más viejo
    List<ExpensesEntity> findByUsuario_IdOrderByDateDescIdDesc(Long usuarioId);
}
