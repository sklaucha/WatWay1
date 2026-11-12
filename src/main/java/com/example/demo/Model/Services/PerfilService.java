package com.example.demo.Model.Services;

import com.example.demo.Model.DTO.UsuarioResponseDTO;
import com.example.demo.Model.DTO.UsuarioUpdateDTO;
import com.example.demo.Model.Entities.UsuarioEntity;
import com.example.demo.Model.Mappers.PerfilMapper;
import com.example.demo.Model.Repository.UsuarioRepository;
import com.example.demo.Seguridad.Repositories.CredentialsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PerfilService {
        private final CredentialsRepository credentialsRepository;
        private final UsuarioRepository usuarioRepository;
        private final PerfilMapper perfilMapper;

        @Transactional(readOnly = true)
        public UsuarioResponseDTO obtenerPerfil(String email) {
            return perfilMapper.toDTO(buscarUsuario(email));
        }

        @Transactional
        public void eliminarCuenta(String email) {
            UsuarioEntity usuario = buscarUsuario(email);
            usuario.setActivo(false);
            usuarioRepository.save(usuario);
        }

        private UsuarioEntity buscarUsuario(String email) {
            UsuarioEntity usuario = credentialsRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"))
                    .getUsuario();

            if (usuario == null || !usuario.isActivo()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
            }
            return usuario;
        }
    @Transactional
    public UsuarioResponseDTO actualizarPerfil(String email, UsuarioUpdateDTO dto) {
        UsuarioEntity usuario = buscarUsuario(email);
        perfilMapper.actualizarEntidad(usuario, dto);
        return perfilMapper.toDTO(usuarioRepository.save(usuario));
    }
}
