package com.example.demo.Seguridad.Repositories;

import com.example.demo.Seguridad.Entities.TokenRevocadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface TokenRevocadoRepository extends JpaRepository<TokenRevocadoEntity, Long> {

    boolean existsByTokenHash(String tokenHash);

    @Modifying
    @Query("DELETE FROM TokenRevocadoEntity t WHERE t.expiracion < :ahora")
    int borrarVencidos(@Param("ahora") LocalDateTime ahora);
}
