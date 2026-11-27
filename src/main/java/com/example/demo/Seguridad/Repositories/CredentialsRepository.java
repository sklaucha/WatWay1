package com.example.demo.Seguridad.Repositories;

import com.example.demo.Seguridad.Entities.CredentialsEntity;
import com.example.demo.Seguridad.Enum.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import java.util.Optional;

public interface CredentialsRepository extends JpaRepository<CredentialsEntity, Long> {
    Optional<CredentialsEntity> findByEmail(String email);
    boolean existsByEmail(String email);

    // Una sola consulta con JOIN FETCH: trae credenciales, usuario, roles y permisos juntos.
    // Evita el problema N+1 (una consulta extra por cada usuario de la lista).
    @Query("SELECT DISTINCT c FROM CredentialsEntity c " +
            "LEFT JOIN FETCH c.usuario " +
            "LEFT JOIN FETCH c.roles r " +
            "LEFT JOIN FETCH r.permits")
    List<CredentialsEntity> findAllConUsuarioYRoles();

    // Cuántas credenciales tienen un rol (por ejemplo, cuántos ADMIN quedan)
    long countByRoles_Role(Role role);
}
