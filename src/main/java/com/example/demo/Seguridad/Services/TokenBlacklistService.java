package com.example.demo.Seguridad.Services;

// INVERSIÓN DE DEPENDENCIAS (SOLID): el filtro JWT y el AuthController dependen
// de esta interfaz, no de cómo se guardan los tokens. Antes era una clase con
// un HashSet en memoria (se perdía al reiniciar y no era seguro con varias
// peticiones a la vez). Ahora la implementación es TokenBlacklistJpaService,
// que guarda en MySQL. Si mañana se usa Redis, se crea otra implementación
// y ninguna de las clases que la usan cambia.
public interface TokenBlacklistService {

    void blacklist(String token);

    boolean isBlacklisted(String token);
}
