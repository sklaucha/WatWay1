package com.example.demo.Model.Mappers;

import com.example.demo.Model.DTO.UsuarioMatchDTO;
import com.example.demo.Model.Entities.MatchEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MatchMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public MatchMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UsuarioMatchDTO toMatchDTO(MatchEntity match) {
        return modelMapper.map(match.getUsuarioDestino(), UsuarioMatchDTO.class);
    }
}