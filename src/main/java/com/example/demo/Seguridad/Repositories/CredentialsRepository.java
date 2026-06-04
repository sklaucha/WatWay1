package com.example.demo.Seguridad.Repositories;

import com.example.demo.Seguridad.Entities.CredentialsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CredentialsRepository extends JpaRepository<CredentialsEntity, Long> {
    Optional<CredentialsEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}