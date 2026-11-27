package com.example.demo.Model.Repository;

import com.example.demo.Model.Entities.UsuarioEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    Optional<UsuarioEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    // Candidatos: misma ciudad, que no evaluaste y que siguen activos
    // (un usuario dado de baja o desactivado por el admin no aparece)
    List<UsuarioEntity> findByCiudadDestinoIgnoreCaseAndIdNotInAndActivoTrue(String ciudadDestino, List<Long> ids);

    // BLOQUEO PESIMISTA: "SELECT ... FOR UPDATE". Mientras dure la transacción,
    // otra transacción que quiera bloquear la misma fila espera su turno.
    // Se usa para que dos pedidos simultáneos del mismo usuario (doble click)
    // se ejecuten de a uno y no dupliquen likes ni denuncias.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM UsuarioEntity u WHERE u.id = :id")
    Optional<UsuarioEntity> bloquear(@Param("id") Long id);
}
