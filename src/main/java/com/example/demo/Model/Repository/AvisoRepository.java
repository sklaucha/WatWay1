package com.example.demo.Model.Repository;

import com.example.demo.Model.Entities.AvisoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvisoRepository extends JpaRepository<AvisoEntity, Long> {

    // Los que ven los usuarios
    List<AvisoEntity> findByActivoTrueOrderByFechaCreacionDesc();

    // Todos, para el panel del admin
    List<AvisoEntity> findAllByOrderByFechaCreacionDesc();
}
