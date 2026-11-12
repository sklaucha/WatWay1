package com.example.demo.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// Lo que ve el admin de cada usuario en la lista.
// Lleva los dos ids porque los endpoints de admin usan uno distinto cada uno:
// - usuarioId    -> DELETE /admin/usuarios/{id}
// - credentialId -> POST   /admin/usuarios/{credentialId}/rol/{rol}
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUsuarioDTO {

    private Long usuarioId;

    private Long credentialId;

    private String nombre;

    private String apellido;

    private String email;

    private String posicionLaboral;

    private String ciudadDestino;

    private boolean activo;

    private List<String> roles;
}
