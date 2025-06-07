package com.Uni9.barberia_project.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;

public record UpdateAgendamentoDto(
        @NotNull(message = "O status não pode ser nulo.")
        @Pattern(regexp = "PENDENTE|CONFIRMADO|CANCELADO", message = "Status inválido. Use PENDENTE, CONFIRMADO ou CANCELADO.")
        String status
) {}
