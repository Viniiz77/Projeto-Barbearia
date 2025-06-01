package com.Uni9.barberia_project.controller;

import com.Uni9.barberia_project.dto.*;
import com.Uni9.barberia_project.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador responsável por lidar com as requisições relacionadas aos usuários.
 * Aqui estão os endpoints para listar, buscar, criar, atualizar e deletar.
 */
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    // Injeta a dependência da classe UsuarioService no controlador.
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> criarUsuario(@Valid @RequestBody CreateUsuarioDto dto) {
        UsuarioResponseDto usuarioCriado = usuarioService.criarUsuario(dto);
        return new ResponseEntity<>(usuarioCriado, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> listarTodosUsuarios() {
        List<UsuarioResponseDto> usuarios = usuarioService.listarTodosUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> buscarUsuarioPorId(@PathVariable Integer id) {
        UsuarioResponseDto usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> atualizarUsuario(@PathVariable Integer id, @Valid @RequestBody UpdateUsuarioDto dto) {
        UsuarioResponseDto usuarioAtualizado = usuarioService.atualizarUsuario(id, dto);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Integer id) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}



