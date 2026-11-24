package com.example.demo.Seguridad.Services;

import com.example.demo.Model.DTO.RegisterRequestDTO;
import com.example.demo.Model.Entities.UsuarioEntity;
import com.example.demo.Model.Excepciones.EmailYaRegistradoException;
import com.example.demo.Model.Repository.UsuarioRepository;
import com.example.demo.Seguridad.Entities.CredentialsEntity;
import com.example.demo.Seguridad.Entities.RoleEntity;
import com.example.demo.Seguridad.Enum.Role;
import com.example.demo.Seguridad.Repositories.CredentialsRepository;
import com.example.demo.Seguridad.Repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// FACADE (GoF): registrar un usuario toca varias piezas (validar el email,
// hashear la contraseña, crear el usuario, crear las credenciales, asignar
// el rol y guardar todo). Desde afuera se ve un solo método: registrar().
// SRP: AuthServiceImpl ya no arma usuarios; solo se ocupa de los tokens.
@Service
@RequiredArgsConstructor
public class RegistroFacade {

    private final UsuarioRepository usuarioRepository;
    private final CredentialsRepository credentialsRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioFactory usuarioFactory;

    // @Transactional: si falla algún paso, no queda un usuario sin credenciales
    @Transactional
    public CredentialsEntity registrar(RegisterRequestDTO request) {
        if (credentialsRepository.existsByEmail(request.getEmail()))
            throw new EmailYaRegistradoException("El email ya está registrado"); // -> 409

        // Se hashea una sola vez y se usa el mismo hash en las dos tablas
        String hash = passwordEncoder.encode(request.getPassword());

        UsuarioEntity usuario = usuarioRepository.save(usuarioFactory.crearUsuario(request, hash));

        // Todo usuario nuevo arranca con rol USER (antes quedaba sin ningún rol)
        RoleEntity rolUser = roleRepository.findByRole(Role.USER)
                .orElseThrow(() -> new IllegalStateException("Falta el rol USER en la base"));

        return credentialsRepository.save(usuarioFactory.crearCredenciales(usuario, hash, rolUser));
    }
}
