package com.Uni9.barberia_project.controller;


import com.Uni9.barberia_project.dto.*;
import com.Uni9.barberia_project.service.UsuarioService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador responsável por lidar com as requisições relacionadas aos usuários.
 * Aqui estão os endpoints para listar, buscar, criar, atualizar e deletar.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/usuarios")
public class UsuarioController {

    // Injeta a dependência da classe UsuarioService no controlador.
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    @Operation(summary = "Cria um novo usuário.",
            description = "Cadastra um novo usuário no sistema, podendo ser Cliente, Barbeiro")
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

    @PostMapping("/login")
    @Operation(summary = "Realiza o login de um usuário (cliente ou barbeiro).",
            description = "Verifica as credenciais (e-mail e senha) e retorna os dados do usuário logado em caso de sucesso. Retorna 400 Bad Request em caso de falha.")
    public ResponseEntity<UsuarioResponseDto> login(@Valid @RequestBody LoginRequestDto loginDto) {
        // A exceção IllegalArgumentException lançada pelo service será capturada pelo GlobalExceptionHandler
        UsuarioResponseDto usuarioLogado = usuarioService.login(loginDto);
        return ResponseEntity.ok(usuarioLogado); // Retorna 200 OK com os dados do usuário
    }
}



