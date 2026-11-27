package com.example.demo.Seguridad.Services;

import com.example.demo.Model.DTO.RegisterRequestDTO;
import com.example.demo.Model.Entities.UsuarioEntity;
import com.example.demo.Seguridad.Entities.CredentialsEntity;
import com.example.demo.Seguridad.Entities.RoleEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

// CREADOR (GRASP) / fábrica simple: es el único lugar que sabe cómo se arma
// un usuario nuevo y sus credenciales a partir del formulario de registro.
// Antes este armado estaba repetido en AuthServiceImpl y en UsuarioServiceImpl.
// No es "Factory Method" del GoF: ese patrón aplica cuando hay que elegir entre
// varias clases concretas; acá siempre se crea el mismo tipo de objeto.
@Component
public class UsuarioFactory {

    public UsuarioEntity crearUsuario(RegisterRequestDTO request, String passwordHasheada) {
        return UsuarioEntity.builder()
                .nombre(request.getNombre().trim())
                .apellido(request.getApellido().trim())
                .email(request.getEmail())
                .password(passwordHasheada)
                .posicionLaboral(request.getPosicionLaboral())
                .ciudadDestino(request.getCiudadDestino())
                .sexo(request.getSexo())
                .descripcion(request.getDescripcion())
                .build();
    }

    public CredentialsEntity crearCredenciales(UsuarioEntity usuario, String passwordHasheada, RoleEntity rol) {
        Set<RoleEntity> roles = new HashSet<>();
        roles.add(rol);
        return CredentialsEntity.builder()
                .email(usuario.getEmail())
                .password(passwordHasheada)
                .usuario(usuario)
                .roles(roles)
                .build();
    }
}
