package com.example.demo.Seguridad.Mapping;

import com.example.demo.Seguridad.Enum.Permit;
import com.example.demo.Seguridad.Enum.Role;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RolePermitMapper {

    private final Map<Role, Set<Permit>> rolePermissions = new EnumMap<>(Role.class);

    public RolePermitMapper() {
        initializeMappings();
    }

    private void initializeMappings() {
        rolePermissions.put(Role.USER, Set.of(
                Permit.USUARIO_VER,
                Permit.USUARIO_MODIFICAR,
                Permit.USUARIO_ELIMINAR,
                Permit.MATCH_VER_CANDIDATOS,
                Permit.MATCH_LIKE,
                Permit.MATCH_DISLIKE,
                Permit.MATCH_VER_MATCHES
        ));

        rolePermissions.put(Role.ADMIN, Set.of(
                Permit.USUARIO_VER,
                Permit.USUARIO_MODIFICAR,
                Permit.USUARIO_ELIMINAR,
                Permit.MATCH_VER_CANDIDATOS,
                Permit.MATCH_LIKE,
                Permit.MATCH_DISLIKE,
                Permit.MATCH_VER_MATCHES,
                Permit.ADMIN_ELIMINAR_USUARIO,
                Permit.ADMIN_LISTAR_USUARIOS
        ));
    }

    public Set<Permit> getPermissionsForRole(Role role) {
        return rolePermissions.getOrDefault(role, Set.of());
    }
}