package com.example.demo.Model.Services.denuncias;

import com.example.demo.Model.Entities.UsuarioEntity;

// Lo que se encontró al buscar lo denunciado: el autor y una copia del texto
public record ContenidoDenunciado(UsuarioEntity autor, String texto) {
}
