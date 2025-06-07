package com.Uni9.barberia_project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Schema(description = "DTO para criar um novo agendamento para barbearia.")
public record CreateAgendamentoDto(
    @NotNull(message= "O Id do cliente é obrigatório")
    @Schema(description = "Id do cliente que está realizando o agendamento.", example = "1")
    Integer clienteId,

    @NotNull(message= "O Id do serviço é obrigatório")
    @Schema(description = "Id do serviço a ser agendado (corte, barba, etc.).", example = "1")
    Integer servicoId,

    @NotNull(message= "Id da barbearia é obrigatório")
    @Schema(description = "Id da barbearia onde o agendamento será realizado.", example = "1")
    Integer barbeariaId,

    @NotNull(message= "A data e a hora do agendamento são obrigatórios" )
    @FutureOrPresent(message= "A data e a hora devem estar no futuro ou no presente")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema(description = "Data e hora exatas do agendamento. Formato: yyyy-MM-ddTHH:mm:ss", example = "2025-07-20T10:30:00")
    LocalDateTime dataHora
) {}
