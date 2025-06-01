package com.Uni9.barberia_project.dto;

import com.Uni9.barberia_project.model.Agendamento.StatusAgendamento;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record UpdateAgendamentoDto(
        @Future(message = "A data e a hora do agendamento devem estar no futuro.")
        LocalDateTime dataHora,

        @NotNull(message = "O status não pode ser nulo.")
        StatusAgendamento status
) {}
