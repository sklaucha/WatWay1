package com.example.demo.Model.Services;

import com.example.demo.Model.DTO.ActualizarUsuarioDTO;
import com.example.demo.Model.DTO.CambiarPasswordDTO;
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
        // Borrado lógico: no se borra la fila porque credentials, matches,
        // mensajes y checklist tienen claves foráneas hacia el usuario.
        // Con activo = false, CredentialsEntity.isEnabled() devuelve false
        // y el usuario ya no puede loguearse ni usar su token.
        UsuarioEntity usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado"));
        // El admin no puede desactivarse a sí mismo: si fuera el único admin,
        // el sistema se quedaría sin nadie que lo administre.
        if (usuario.getId().equals(getUsuarioActual().getId()))
            throw new IllegalArgumentException("No podés desactivar tu propia cuenta desde el panel de admin");
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    @Override
    // Baja de la cuenta, confirmada con la contraseña.
    // Es un borrado lógico (activo = false), igual que la baja que hace el admin:
    // la fila no se puede borrar porque checklist, matches y mensajes la referencian.
    // Con activo = false el login queda bloqueado (CredentialsEntity.isEnabled).
    public void eliminarCuenta(String password) {
        CredentialsEntity credentials = getCredencialesActuales();
        verificarPassword(password, credentials, "La contraseña es incorrecta");

        UsuarioEntity usuario = getUsuarioActual();
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    // ---------- Perfil del usuario logueado ----------

    @Override
    public UsuarioResponseDTO obtenerMiPerfil() {
        return usuarioMapper.toResponseDTO(getUsuarioActual());
    }

    @Override
    public UsuarioResponseDTO actualizarMiPerfil(ActualizarUsuarioDTO request) {
        UsuarioEntity usuario = getUsuarioActual();
        usuario.setNombre(request.getNombre().trim());
        usuario.setApellido(request.getApellido().trim());
        usuario.setPosicionLaboral(request.getPosicionLaboral());
        usuario.setCiudadDestino(request.getCiudadDestino());
        usuario.setSexo(request.getSexo());
        usuario.setDescripcion(request.getDescripcion());
        return usuarioMapper.toResponseDTO(usuarioRepository.save(usuario));
    }

    // Cambia la contraseña pidiendo la actual
    @Override
    public void cambiarPassword(CambiarPasswordDTO request) {
        CredentialsEntity credentials = getCredencialesActuales();
        verificarPassword(request.getActual(), credentials, "La contraseña actual es incorrecta");

        if (passwordEncoder.matches(request.getNueva(), credentials.getPassword()))
            throw new IllegalArgumentException("La nueva contraseña tiene que ser distinta a la actual");

        String hash = passwordEncoder.encode(request.getNueva());
        credentials.setPassword(hash);
        credentialsRepository.save(credentials);

        // La tabla usuario también guarda la contraseña: la mantenemos igual
        UsuarioEntity usuario = getUsuarioActual();
        usuario.setPassword(hash);
        usuarioRepository.save(usuario);
    }

    // Compara la contraseña escrita con el hash BCrypt guardado.
    // Si no coincide -> 400 con el mensaje (no 401, porque la sesión sigue siendo válida).
    private void verificarPassword(String password, CredentialsEntity credentials, String mensajeError) {
        if (password == null || !passwordEncoder.matches(password, credentials.getPassword()))
            throw new IllegalArgumentException(mensajeError);
    }

    // Se vuelve a buscar en la base para tener la versión actual (con la contraseña vigente)
    private CredentialsEntity getCredencialesActuales() {
        CredentialsEntity logueado = (CredentialsEntity)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return credentialsRepository.findByEmail(logueado.getEmail())
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado"));
    }

    // El usuario sale del token: nadie puede leer ni editar el perfil de otro
    private UsuarioEntity getUsuarioActual() {
        CredentialsEntity credentials = (CredentialsEntity)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return usuarioRepository.findByEmail(credentials.getEmail())
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado"));
    }
}