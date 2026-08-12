package com.example.demo.Model.Repository;

import com.example.demo.Model.Entities.Checklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChecklistRepository extends JpaRepository <Checklist, Long> {
    Optional<Checklist> findByUsuarioId(Long usuarioId);
    Optional<Checklist> findByTrabajoId(Long trabajoId);
}
