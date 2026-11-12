package com.example.demo.Model.Mappers;
import com.example.demo.Model.DTO.UsuarioResponseDTO;
import com.example.demo.Model.DTO.UsuarioUpdateDTO;
import com.example.demo.Model.Entities.UsuarioEntity;
import org.springframework.stereotype.Component;

@Component
public class PerfilMapper {
    public UsuarioResponseDTO toDTO(UsuarioEntity u) {
        return UsuarioResponseDTO.builder()
                .id(u.getId())
                .nombre(u.getNombre())
                .apellido(u.getApellido())
                .email(u.getEmail())
                .posicionLaboral(u.getPosicionLaboral())
                .ciudadDestino(u.getCiudadDestino())
                .sexo(u.getSexo())
                .descripcion(u.getDescripcion())
                .build();
    }
    public void actualizarEntidad(UsuarioEntity u, UsuarioUpdateDTO dto) {
        u.setNombre(dto.getNombre().trim());
        u.setApellido(dto.getApellido().trim());
        u.setPosicionLaboral(dto.getPosicionLaboral().trim());
        u.setCiudadDestino(dto.getCiudadDestino().trim());
        u.setSexo(dto.getSexo());
        u.setDescripcion(dto.getDescripcion());
    }
}
