package com.example.demo.Model.Services;

import com.example.demo.Model.DTO.AdminUsuarioDTO;
import com.example.demo.Model.Entities.UsuarioEntity;
import com.example.demo.Model.Excepciones.UsuarioNotFoundException;
import com.example.demo.Model.Repository.UsuarioRepository;
import com.example.demo.Seguridad.Entities.CredentialsEntity;
import com.example.demo.Seguridad.Entities.RoleEntity;
import com.example.demo.Seguridad.Enum.Role;
import com.example.demo.Seguridad.Repositories.CredentialsRepository;
import com.example.demo.Seguridad.Repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final CredentialsRepository credentialsRepository;
    private final RoleRepository roleRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<AdminUsuarioDTO> listarUsuarios() {
        // Armamos un "diccionario" usuarioId -> credenciales,
        // así no hacemos una consulta a la base por cada usuario.
        Map<Long, CredentialsEntity> credencialesPorUsuario = credentialsRepository.findAll().stream()
                .filter(c -> c.getUsuario() != null)
                .collect(Collectors.toMap(c -> c.getUsuario().getId(), c -> c));

        return usuarioRepository.findAll().stream()
                .map(usuario -> toAdminDTO(usuario, credencialesPorUsuario.get(usuario.getId())))
                .toList();
    }

    // Deshace el borrado lógico: el usuario puede volver a loguearse
    public void reactivarUsuario(Long usuarioId) {
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado"));
        usuario.setActivo(true);
        usuarioRepository.save(usuario);
    }

    public void asignarRol(Long credentialId, String rolNombre) {
        CredentialsEntity credentials = credentialsRepository.findById(credentialId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Role role = Role.valueOf(rolNombre.toUpperCase());
        RoleEntity roleEntity = roleRepository.findByRole(role)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        credentials.getRoles().add(roleEntity);
        credentialsRepository.save(credentials);
    }

    private AdminUsuarioDTO toAdminDTO(UsuarioEntity usuario, CredentialsEntity credentials) {
        List<String> roles = credentials == null
                ? List.of()
                : credentials.getRoles().stream()
                    .map(r -> r.getRole().name())
                    .sorted()
                    .toList();

        return AdminUsuarioDTO.builder()
                .usuarioId(usuario.getId())
                .credentialId(credentials != null ? credentials.getId() : null)
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail())
                .posicionLaboral(usuario.getPosicionLaboral())
                .ciudadDestino(usuario.getCiudadDestino())
                .activo(usuario.isActivo())
                .roles(roles)
                .build();
    }
}
