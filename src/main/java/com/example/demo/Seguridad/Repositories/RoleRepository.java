package com.example.demo.Seguridad.Repositories;

import com.example.demo.Seguridad.Entities.RoleEntity;
import com.example.demo.Seguridad.Enum.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    boolean existsByRole(Role role);
    Optional<RoleEntity> findByRole(Role role);
}