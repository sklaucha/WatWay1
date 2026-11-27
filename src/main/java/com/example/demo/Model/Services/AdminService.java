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
    private final UsuarioActualProvider usuarioActual;

    @Transactional(readOnly = true)
    public List<AdminUsuarioDTO> listarUsuarios() {
        // Armamos un "diccionario" usuarioId -> credenciales,
        // así no hacemos una consulta a la base por cada usuario.
        Map<Long, CredentialsEntity> credencialesPorUsuario = credentialsRepository.findAllConUsuarioYRoles().stream()
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
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado")); // -> 404

        Role role;
        try {
            role = Role.valueOf(rolNombre.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Rol inválido"); // -> 400, sin mostrar nombres internos
        }
        RoleEntity roleEntity = roleRepository.findByRole(role)
                .orElseThrow(() -> new IllegalArgumentException("Rol inválido"));

        credentials.getRoles().add(roleEntity);
        credentialsRepository.save(credentials);
    }

    // Quita un rol. Para ADMIN hay dos reglas:
    //  - un admin no se puede quitar el rol a sí mismo (evita quedarse afuera por error)
    //  - no se puede quitar el último admin (el sistema quedaría sin administrador)
    // El cambio rige desde la próxima petición: el filtro JWT lee los roles de la base.
    public void quitarRol(Long credentialId, String rolNombre) {
        CredentialsEntity credentials = credentialsRepository.findById(credentialId)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado")); // -> 404

        Role role;
        try {
            role = Role.valueOf(rolNombre.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Rol inválido"); // -> 400
        }

        boolean loTiene = credentials.getRoles().stream().anyMatch(r -> r.getRole() == role);
        if (!loTiene)
            throw new IllegalArgumentException("El usuario no tiene el rol " + role.name());

        if (role == Role.ADMIN) {
            if (credentials.getId().equals(usuarioActual.getCredenciales().getId()))
                throw new IllegalArgumentException("No podés quitarte el rol de administrador a vos mismo");
            if (credentialsRepository.countByRoles_Role(Role.ADMIN) <= 1)
                throw new IllegalArgumentException("No se puede quitar el último administrador del sistema");
        }

        credentials.getRoles().removeIf(r -> r.getRole() == role);
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
