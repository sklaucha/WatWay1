package com.example.demo.Seguridad.Services;

import com.example.demo.Seguridad.Entities.TokenRevocadoEntity;
import com.example.demo.Seguridad.Repositories.TokenRevocadoRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HexFormat;

// Implementación de la lista negra que guarda los tokens revocados en MySQL.
// Así un logout sigue valiendo aunque se reinicie el back.
@Service
@RequiredArgsConstructor
public class TokenBlacklistJpaService implements TokenBlacklistService {

    private final TokenRevocadoRepository tokenRevocadoRepository;
    private final JwtService jwtService;

    @Override
    public void blacklist(String token) {
        if (token == null || token.isBlank()) return;

        Date expira;
        try {
            expira = jwtService.extractClaim(token, Claims::getExpiration);
        } catch (RuntimeException e) {
            // Token vencido o inválido: ya no sirve para entrar, no hace falta guardarlo
            return;
        }

        String hash = sha256(token);
        if (tokenRevocadoRepository.existsByTokenHash(hash)) return;

        try {
            tokenRevocadoRepository.save(TokenRevocadoEntity.builder()
                    .tokenHash(hash)
                    .expiracion(LocalDateTime.ofInstant(expira.toInstant(), ZoneId.systemDefault()))
                    .build());
        } catch (DataIntegrityViolationException e) {
            // Dos peticiones revocaron el mismo token a la vez: ya quedó guardado
        }
    }

    @Override
    public boolean isBlacklisted(String token) {
        if (token == null || token.isBlank()) return false;
        return tokenRevocadoRepository.existsByTokenHash(sha256(token));
    }

    // Cada una hora borra los tokens que ya vencieron: aunque no estuvieran
    // en la lista, no servirían para entrar. Así la tabla no crece sin fin.
    @Scheduled(fixedRate = 60 * 60 * 1000)
    @Transactional
    public void limpiarVencidos() {
        tokenRevocadoRepository.borrarVencidos(LocalDateTime.now());
    }

    // Se guarda el hash SHA-256 (64 caracteres fijos) y no el token completo:
    // ocupa menos, se puede indexar y en la base no queda ningún token usable.
    private String sha256(String token) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no disponible", e);
        }
    }
}
