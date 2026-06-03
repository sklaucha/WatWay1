package Model.Services;

import Model.DTO.RegisterRequestDTO;
import Model.DTO.UsuarioResponseDTO;

import java.util.List;

public interface UsuarioService {

    UsuarioResponseDTO crearUsuario(RegisterRequestDTO request);

    UsuarioResponseDTO obtenerUsuario(Long id);

    List<UsuarioResponseDTO> listarUsuarios();

    void eliminarUsuario(Long id);
}