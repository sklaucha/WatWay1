package com.example.demo.Model.Repository;

import com.example.demo.Model.Entities.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    Optional<UsuarioEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    List<UsuarioEntity> findByCiudadDestinoIgnoreCaseAndIdNotIn(String ciudadDestino, List<Long> ids);

}