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

    public UsuarioResponseDTO toResponseDTO(UsuarioEntity usuario) {
        return modelMapper.map(usuario, UsuarioResponseDTO.class);
    }

    public UsuarioMatchDTO toMatchDTO(UsuarioEntity usuario) {
        return modelMapper.map(usuario, UsuarioMatchDTO.class);
    }

    public UsuarioEntity toEntity(UsuarioResponseDTO dto) {
        return modelMapper.map(dto, UsuarioEntity.class);
    }
}