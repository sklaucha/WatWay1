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

        List<Long> yaEvaluados = matchRepository.findUsuariosEvaluadosByOrigen(actual);
        yaEvaluados.add(actual.getId());

        return usuarioRepository.findByCiudadDestinoIgnoreCaseAndIdNotIn(
                        actual.getCiudadDestino(), yaEvaluados)
                .stream()
                .map(this::toMatchDTO)
                .collect(Collectors.toList());
    }

    public String like(Long usuarioDestinoId) {
        UsuarioEntity origen = getUsuarioActual();
        UsuarioEntity destino = usuarioRepository.findById(usuarioDestinoId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (matchRepository.existsByUsuarioOrigenAndUsuarioDestino(origen, destino)) {
            return "Ya evaluaste a este usuario anteriormente.";
        }

        MatchEntity match = MatchEntity.builder()
                .usuarioOrigen(origen)
                .usuarioDestino(destino)
                .liked(true)
                .matched(false)
                .build();
        matchRepository.save(match);

        List<MatchEntity> likeRecíproco = matchRepository
                .findByUsuarioOrigenAndUsuarioDestino(destino, origen);

        if (!likeRecíproco.isEmpty() && likeRecíproco.get(0).isLiked()) {
            match.setMatched(true);
            likeRecíproco.get(0).setMatched(true);
            matchRepository.save(match);
            matchRepository.save(likeRecíproco.get(0));
            return "¡Es un match!";
        }

        return "Like enviado";
    }

    public String dislike(Long usuarioDestinoId) {
        UsuarioEntity origen = getUsuarioActual();
        UsuarioEntity destino = usuarioRepository.findById(usuarioDestinoId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (matchRepository.existsByUsuarioOrigenAndUsuarioDestino(origen, destino)) {
            return "Ya evaluaste a este usuario anteriormente.";
        }

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