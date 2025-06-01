package com.Uni9.barberia_project.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateAgendamentoDto(
    @NotNull(message= "O ID do cliente é obrigatório")
    Integer clienteId,

    @NotNull(message= "O ID do barbeiro é obrigatório")
    Integer barbeiroId,

    @NotNull(message= "O ID do serviço é obrigatório" )
    Integer servicoId,

    @NotNull(message= "O ID da barbearia é obrigatório")
    Integer barbeariaId,

    @NotNull(message= "A data e a hora do agendamento são obrigatórios" )
    @Future(message= "A data e a hora devem estar no futuro")
    LocalDateTime dataHora
) {}
