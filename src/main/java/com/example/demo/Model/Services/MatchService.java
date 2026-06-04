package com.example.demo.Model.Services;

import com.example.demo.Model.DTO.UsuarioMatchDTO;
import com.example.demo.Model.Entities.MatchEntity;
import com.example.demo.Model.Entities.UsuarioEntity;
import com.example.demo.Model.Repository.MatchRepository;
import com.example.demo.Model.Repository.UsuarioRepository;
import com.example.demo.Seguridad.Entities.CredentialsEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final UsuarioRepository usuarioRepository;
    private final MatchRepository matchRepository;

    private UsuarioEntity getUsuarioActual() {
        CredentialsEntity credentials = (CredentialsEntity)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return usuarioRepository.findByEmail(credentials.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public List<UsuarioMatchDTO> getCandidatos() {
        UsuarioEntity actual = getUsuarioActual();

        // Usuarios que ya fueron evaluados
        List<Long> yaEvaluados = matchRepository
                .findAll()
                .stream()
                .filter(m -> m.getUsuarioOrigen().getId().equals(actual.getId()))
                .map(m -> m.getUsuarioDestino().getId())
                .collect(Collectors.toList());

        yaEvaluados.add(actual.getId()); // excluir a sí mismo

        return usuarioRepository.findAll()
                .stream()
                .filter(u -> !yaEvaluados.contains(u.getId()))
                .filter(u -> u.getCiudadDestino().equalsIgnoreCase(actual.getCiudadDestino()))
                .map(this::toMatchDTO)
                .collect(Collectors.toList());
    }

    public String like(Long usuarioDestinoId) {
        UsuarioEntity origen = getUsuarioActual();
        UsuarioEntity destino = usuarioRepository.findById(usuarioDestinoId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Guardar el like
        MatchEntity match = MatchEntity.builder()
                .usuarioOrigen(origen)
                .usuarioDestino(destino)
                .liked(true)
                .matched(false)
                .build();
        matchRepository.save(match);

        // Ver si el otro también dio like
        Optional<MatchEntity> likeRecíproco = matchRepository
                .findByUsuarioOrigenAndUsuarioDestino(destino, origen);

        if (likeRecíproco.isPresent() && likeRecíproco.get().isLiked()) {
            // Es un match — actualizar ambos
            match.setMatched(true);
            likeRecíproco.get().setMatched(true);
            matchRepository.save(match);
            matchRepository.save(likeRecíproco.get());
            return "¡Es un match!";
        }

        return "Like enviado";
    }

    public String dislike(Long usuarioDestinoId) {
        UsuarioEntity origen = getUsuarioActual();
        UsuarioEntity destino = usuarioRepository.findById(usuarioDestinoId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        MatchEntity match = MatchEntity.builder()
                .usuarioOrigen(origen)
                .usuarioDestino(destino)
                .liked(false)
                .matched(false)
                .build();
        matchRepository.save(match);

        return "Dislike registrado";
    }

    public List<UsuarioMatchDTO> getMatches() {
        UsuarioEntity actual = getUsuarioActual();

        List<UsuarioEntity> matchesComoOrigen = matchRepository
                .findByUsuarioOrigenAndMatched(actual, true)
                .stream()
                .map(MatchEntity::getUsuarioDestino)
                .collect(Collectors.toList());

        List<UsuarioEntity> matchesComoDestino = matchRepository
                .findByUsuarioDestinoAndMatched(actual, true)
                .stream()
                .map(MatchEntity::getUsuarioOrigen)
                .collect(Collectors.toList());

        return Stream.concat(matchesComoOrigen.stream(), matchesComoDestino.stream())
                .distinct()
                .map(this::toMatchDTO)
                .collect(Collectors.toList());
    }

    private UsuarioMatchDTO toMatchDTO(UsuarioEntity u) {
        return UsuarioMatchDTO.builder()
                .id(u.getId())
                .nombre(u.getNombre())
                .apellido(u.getApellido())
                .sexo(u.getSexo())
                .posicionLaboral(u.getPosicionLaboral())
                .ciudadDestino(u.getCiudadDestino())
                .descripcion(u.getDescripcion())
                .build();
    }
}