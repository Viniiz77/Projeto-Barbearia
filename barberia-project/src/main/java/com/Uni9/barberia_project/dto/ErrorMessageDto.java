package com.Uni9.barberia_project.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO padrão para mensagens de erro da API.")
public record ErrorMessageDto(
        @Schema(description = "Mensagem de erro detalhada.", example = "Email já está em uso.")
        String message,
        @Schema(description = "Código de status HTTP da resposta.", example = "400")
        int status
) {}
