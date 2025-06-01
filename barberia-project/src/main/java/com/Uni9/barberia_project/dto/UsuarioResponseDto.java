package com.Uni9.barberia_project.dto;

import com.Uni9.barberia_project.model.Usuario;

import java.time.LocalDateTime;

public record UsuarioResponseDto(
        Integer id,
        String nome,
        String email,
        String telefone,
        Usuario.TipoUsuario tipo,
        String nomeBarbearia,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {}
