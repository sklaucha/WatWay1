package com.example.demo.Model.Controller;

import com.example.demo.Model.Entities.UsuarioEntity;
import com.example.demo.Model.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    @GetMapping
    public List<UsuarioEntity> getAll() {
        return usuarioRepository.findAll();
    }
}