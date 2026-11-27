package com.example.demo.Model.Eventos;

import com.example.demo.Model.DTO.AvisoDTO;

// OBSERVER (GoF): este es el "evento" que se difunde cuando se publica un aviso.
// AvisoService lo publica sin saber quién lo escucha.
public record AvisoPublicadoEvent(AvisoDTO aviso) {
}
