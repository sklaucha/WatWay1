package com.example.demo.Model.Services;

import com.example.demo.Model.DTO.UsuarioMatchDTO;
import com.example.demo.Model.Entities.MatchEntity;
import com.example.demo.Model.Entities.UsuarioEntity;
import com.example.demo.Model.Exceptions.SinCandidatosException;
import com.example.demo.Model.Excepciones.UsuarioNotFoundException;
import com.example.demo.Model.Excepciones.UsuarioYaEvaluadoException;
import com.example.demo.Model.Mappers.UsuarioMapper;
import com.example.demo.Model.Repository.MatchRepository;
import com.example.demo.Model.Repository.UsuarioRepository;
import com.example.demo.Seguridad.Entities.CredentialsEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final UsuarioRepository usuarioRepository;
    private final MatchRepository matchRepository;
    private final UsuarioMapper usuarioMapper;

    private UsuarioEntity getUsuarioActual() {
        CredentialsEntity credentials = (CredentialsEntity)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return usuarioRepository.findByEmail(credentials.getEmail())
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado"));
    }

    public List<UsuarioMatchDTO> getCandidatos() {
        UsuarioEntity actual = getUsuarioActual();
        List<Long> yaEvaluados = matchRepository.findUsuariosEvaluadosByOrigen(actual);
        yaEvaluados.add(actual.getId());
        return usuarioRepository.findByCiudadDestinoIgnoreCaseAndIdNotIn(
                        actual.getCiudadDestino(), yaEvaluados)
                .stream()
                .map(usuarioMapper::toMatchDTO)
                .collect(Collectors.toList());
    }

    public UsuarioMatchDTO getCandidato() {
        UsuarioEntity actual = getUsuarioActual();
        List<Long> yaEvaluados = matchRepository.findUsuariosEvaluadosByOrigen(actual);
        yaEvaluados.add(actual.getId());
        List<UsuarioEntity> candidatos = new ArrayList<>(usuarioRepository
                .findByCiudadDestinoIgnoreCaseAndIdNotIn(actual.getCiudadDestino(), yaEvaluados));
        if (candidatos.isEmpty())
            throw new SinCandidatosException("No hay más candidatos disponibles.");
        Collections.shuffle(candidatos);
        return usuarioMapper.toMatchDTO(candidatos.get(0));
    }

    public String like(Long usuarioDestinoId) {
        UsuarioEntity origen = getUsuarioActual();

        if (origen.getId().equals(usuarioDestinoId))
            throw new RuntimeException("No podés darte like a vos mismo.");

        UsuarioEntity destino = usuarioRepository.findById(usuarioDestinoId)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado"));
        if (matchRepository.existsByUsuarioOrigenAndUsuarioDestino(origen, destino))
            throw new UsuarioYaEvaluadoException("Ya evaluaste a este usuario anteriormente.");
        MatchEntity match = MatchEntity.builder()
                .usuarioOrigen(origen).usuarioDestino(destino).liked(true).matched(false).build();
        matchRepository.save(match);
        List<MatchEntity> likeRecíproco = matchRepository.findByUsuarioOrigenAndUsuarioDestino(destino, origen);
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

        if (origen.getId().equals(usuarioDestinoId))
            throw new RuntimeException("No podés darte dislike a vos mismo.");

        UsuarioEntity destino = usuarioRepository.findById(usuarioDestinoId)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado"));
        if (matchRepository.existsByUsuarioOrigenAndUsuarioDestino(origen, destino))
            throw new UsuarioYaEvaluadoException("Ya evaluaste a este usuario anteriormente.");
        matchRepository.save(MatchEntity.builder()
                .usuarioOrigen(origen).usuarioDestino(destino).liked(false).matched(false).build());
        return "Dislike registrado";
    }

    public List<UsuarioMatchDTO> getMatches() {
        UsuarioEntity actual = getUsuarioActual();
        List<UsuarioEntity> matchesComoOrigen = matchRepository
                .findByUsuarioOrigenAndMatched(actual, true).stream()
                .map(MatchEntity::getUsuarioDestino).collect(Collectors.toList());
        List<UsuarioEntity> matchesComoDestino = matchRepository
                .findByUsuarioDestinoAndMatched(actual, true).stream()
                .map(MatchEntity::getUsuarioOrigen).collect(Collectors.toList());
        return Stream.concat(matchesComoOrigen.stream(), matchesComoDestino.stream())
                .distinct().map(usuarioMapper::toMatchDTO).collect(Collectors.toList());
    }
}