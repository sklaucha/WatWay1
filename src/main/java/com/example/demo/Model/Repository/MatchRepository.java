package com.example.demo.Model.Repository;

import com.example.demo.Model.Entities.MatchEntity;
import com.example.demo.Model.Entities.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchRepository extends JpaRepository<MatchEntity, Long> {

    List<MatchEntity> findByUsuarioOrigenAndUsuarioDestino(
            UsuarioEntity usuarioOrigen, UsuarioEntity usuarioDestino);

    List<MatchEntity> findByUsuarioOrigenAndMatched(UsuarioEntity usuarioOrigen, boolean matched);

    List<MatchEntity> findByUsuarioDestinoAndMatched(UsuarioEntity usuarioDestino, boolean matched);

    @Query("SELECT m FROM MatchEntity m WHERE m.usuarioOrigen = :usuario AND m.liked = false")
    List<MatchEntity> findDislikesByUsuario(@Param("usuario") UsuarioEntity usuario);

    boolean existsByUsuarioOrigenAndUsuarioDestino(
            UsuarioEntity usuarioOrigen, UsuarioEntity usuarioDestino);

    @Query("SELECT m.usuarioDestino.id FROM MatchEntity m WHERE m.usuarioOrigen = :usuario")
    List<Long> findUsuariosEvaluadosByOrigen(@Param("usuario") UsuarioEntity usuario);
}