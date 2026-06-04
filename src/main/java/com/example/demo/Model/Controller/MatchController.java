package com.example.demo.Model.Controller;

import com.example.demo.Model.DTO.UsuarioMatchDTO;
import com.example.demo.Model.Services.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/match")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @GetMapping("/candidatos")
    public ResponseEntity<List<UsuarioMatchDTO>> getCandidatos() {
        return ResponseEntity.ok(matchService.getCandidatos());
    }

    @PostMapping("/like/{usuarioId}")
    public ResponseEntity<String> like(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(matchService.like(usuarioId));
    }

    @PostMapping("/dislike/{usuarioId}")
    public ResponseEntity<String> dislike(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(matchService.dislike(usuarioId));
    }

    @GetMapping("/matches")
    public ResponseEntity<List<UsuarioMatchDTO>> getMatches() {
        return ResponseEntity.ok(matchService.getMatches());
    }
}