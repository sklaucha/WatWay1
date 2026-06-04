package com.example.demo.Seguridad.Enum;

public enum Permit {
    // Usuario
    USUARIO_VER,
    USUARIO_MODIFICAR,
    USUARIO_ELIMINAR,

    // Matching
    MATCH_VER_CANDIDATOS,
    MATCH_LIKE,
    MATCH_DISLIKE,
    MATCH_VER_MATCHES,

    // Admin
    ADMIN_ELIMINAR_USUARIO,
    ADMIN_LISTAR_USUARIOS
}