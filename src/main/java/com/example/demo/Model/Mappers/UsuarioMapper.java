package com.example.demo.Model.Mappers;

import com.example.demo.Model.DTO.UsuarioMatchDTO;
import com.example.demo.Model.DTO.UsuarioResponseDTO;
import com.example.demo.Model.Entities.UsuarioEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public UsuarioMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    // Mapeo manual: no depende de ModelMapper ni de constructores sin argumentos.
    public UsuarioResponseDTO toResponseDTO(UsuarioEntity usuario) {
        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail())
                .posicionLaboral(usuario.getPosicionLaboral())
                .ciudadDestino(usuario.getCiudadDestino())
                .sexo(usuario.getSexo())
                .descripcion(usuario.getDescripcion())
                .build();
    }

    // Mapeo manual: es el que fallaba con ModelMapper.
    public UsuarioMatchDTO toMatchDTO(UsuarioEntity usuario) {
        return UsuarioMatchDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .sexo(usuario.getSexo())
                .posicionLaboral(usuario.getPosicionLaboral())
                .ciudadDestino(usuario.getCiudadDestino())
                .descripcion(usuario.getDescripcion())
                .build();
    }

    public UsuarioEntity toEntity(UsuarioResponseDTO dto) {
        return modelMapper.map(dto, UsuarioEntity.class);
    }
}