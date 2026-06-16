package com.example.demo.Model.Services;

import com.example.demo.Model.DTO.RegisterRequestDTO;
import com.example.demo.Model.DTO.UsuarioResponseDTO;
import com.example.demo.Model.Entities.UsuarioEntity;
import com.example.demo.Model.Excepciones.EmailYaRegistradoException;
import com.example.demo.Model.Excepciones.UsuarioNotFoundException;
import com.example.demo.Model.Mappers.UsuarioMapper;
import com.example.demo.Model.Repository.UsuarioRepository;
import com.example.demo.Seguridad.Entities.CredentialsEntity;
import com.example.demo.Seguridad.Repositories.CredentialsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CredentialsRepository credentialsRepository;
    private final UsuarioMapper usuarioMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UsuarioResponseDTO crearUsuario(RegisterRequestDTO request) {
        if (usuarioRepository.existsByEmail(request.getEmail()))
            throw new EmailYaRegistradoException("El email ya está registrado");

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

        return usuarioMapper.toResponseDTO(usuarioRepository.save(usuario));
    }

    @Override
    public UsuarioResponseDTO obtenerUsuario(Long id) {
        return usuarioMapper.toResponseDTO(usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado")));
    }

    @Override
    public List<UsuarioResponseDTO> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toResponseDTO)
                .toList();
    }

    @Override
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id))
            throw new UsuarioNotFoundException("Usuario no encontrado");
        usuarioRepository.deleteById(id);
    }

    @Override
    public void eliminarCuenta() {
        CredentialsEntity credentials = (CredentialsEntity)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UsuarioEntity usuario = usuarioRepository.findByEmail(credentials.getEmail())
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado"));
        credentialsRepository.delete(credentials);
        usuarioRepository.delete(usuario);
    }
}