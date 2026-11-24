package com.example.demo.Model.Repository;

import com.example.demo.Model.Entities.ForoRespuestaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ForoRespuestaRepository extends JpaRepository<ForoRespuestaEntity, Long> {

    List<ForoRespuestaEntity> findByTema_IdOrderByFechaCreacionAsc(Long temaId);

    long countByTema_Id(Long temaId);

    // Se usa al borrar un tema, para borrar antes sus respuestas
    void deleteByTema_Id(Long temaId);
}
