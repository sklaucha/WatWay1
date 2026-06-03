package Services.Impl;

import Model.DTO.RegisterRequestDTO;
import Model.DTO.UsuarioResponseDTO;
import Model.Entities.UsuarioEntity;
import Model.Repositories.UsuarioRepository;
import Model.Services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    @Override
    public UsuarioResponseDTO crearUsuario(RegisterRequestDTO request) {

        if(usuarioRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("El email ya está registrado");
        }

        UsuarioEntity usuario = UsuarioEntity.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .posicionLaboral(request.getPosicionLaboral())
                .ciudadDestino(request.getCiudadDestino())
                .sexo(request.getSexo())
                .descripcion(request.getDescripcion())
                .build();

        usuario = usuarioRepository.save(usuario);

        return convertir(usuario);
    }

    @Override
    public UsuarioResponseDTO obtenerUsuario(Long id) {

        UsuarioEntity usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return convertir(usuario);
    }

    @Override
    public List<UsuarioResponseDTO> listarUsuarios() {

        return usuarioRepository.findAll()
                .stream()
                .map(this::convertir)
                .toList();
    }

    @Override
    public void eliminarUsuario(Long id) {

        usuarioRepository.deleteById(id);
    }

    private UsuarioResponseDTO convertir(UsuarioEntity usuario){

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
}