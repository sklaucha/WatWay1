package com.example.demo.Model.Services;

import com.example.demo.Model.Entities.UsuarioEntity;
import com.example.demo.Model.Excepciones.UsuarioNotFoundException;
import com.example.demo.Model.Repository.UsuarioRepository;
import com.example.demo.Seguridad.Entities.CredentialsEntity;
import com.example.demo.Seguridad.Repositories.CredentialsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

// FABRICACIÓN PURA (GRASP): no es parte del negocio de WatWay, es una clase
// técnica que sabe leer quién está logueado desde Spring Security.
// INVERSIÓN DE DEPENDENCIAS (SOLID): los services la reciben por constructor
// y no tocan SecurityContextHolder directamente. Si mañana cambia la forma
// de autenticar, se modifica solo esta clase.
// Antes este código estaba copiado en 6 services distintos.
@Component
@RequiredArgsConstructor
public class UsuarioActualProvider {

    private final UsuarioRepository usuarioRepository;
    private final CredentialsRepository credentialsRepository;

    private String emailLogueado() {
        CredentialsEntity credentials = (CredentialsEntity)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return credentials.getEmail();
    }

    // Datos del usuario (nombre, ciudad, etc.)
    public UsuarioEntity getUsuario() {
        return usuarioRepository.findByEmail(emailLogueado())
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado"));
    }

    // Credenciales (email, contraseña y roles). Se vuelven a buscar en la base
    // para tener la versión actual, por ejemplo con la contraseña vigente.
    public CredentialsEntity getCredenciales() {
        return credentialsRepository.findByEmail(emailLogueado())
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado"));
    }

    public boolean esAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"));
    }
}
