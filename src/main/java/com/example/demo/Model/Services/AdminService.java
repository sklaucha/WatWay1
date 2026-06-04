package com.example.demo.Model.Services;

import com.example.demo.Seguridad.Entities.CredentialsEntity;
import com.example.demo.Seguridad.Entities.RoleEntity;
import com.example.demo.Seguridad.Enum.Role;
import com.example.demo.Seguridad.Repositories.CredentialsRepository;
import com.example.demo.Seguridad.Repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final CredentialsRepository credentialsRepository;
    private final RoleRepository roleRepository;

    public void asignarRol(Long credentialId, String rolNombre) {
        CredentialsEntity credentials = credentialsRepository.findById(credentialId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Role role = Role.valueOf(rolNombre.toUpperCase());
        RoleEntity roleEntity = roleRepository.findByRole(role)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        credentials.getRoles().add(roleEntity);
        credentialsRepository.save(credentials);
    }
}