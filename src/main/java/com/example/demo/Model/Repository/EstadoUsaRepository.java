package com.example.demo.Model.Repository;

import com.example.demo.Model.Entities.EstadoUsaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstadoUsaRepository extends JpaRepository<EstadoUsaEntity, String> {

    List<EstadoUsaEntity> findAllByOrderByNombreAsc();
}
