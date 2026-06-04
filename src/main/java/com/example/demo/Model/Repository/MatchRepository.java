package com.example.demo.Model.Repository;

import com.example.demo.Model.Entities.MatchEntity;
import com.example.demo.Model.Entities.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<MatchEntity, Long> {

    Optional<MatchEntity> findByUsuarioOrigenAndUsuarioDestino(
            UsuarioEntity usuarioOrigen, UsuarioEntity usuarioDestino);

    List<MatchEntity> findByUsuarioOrigenAndMatched(UsuarioEntity usuarioOrigen, boolean matched);

    List<MatchEntity> findByUsuarioDestinoAndMatched(UsuarioEntity usuarioDestino, boolean matched);

    @Query("SELECT m FROM MatchEntity m WHERE m.usuarioOrigen = :usuario AND m.liked = false")
    List<MatchEntity> findDislikesByUsuario(@Param("usuario") UsuarioEntity usuario);

    boolean existsByUsuarioOrigenAndUsuarioDestino(
            UsuarioEntity usuarioOrigen, UsuarioEntity usuarioDestino);
}