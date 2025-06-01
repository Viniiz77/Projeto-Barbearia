package com.Uni9.barberia_project.dto;

import com.Uni9.barberia_project.model.Agendamento;
import com.Uni9.barberia_project.model.Agendamento.StatusAgendamento;

import java.time.LocalDateTime;

public record AgendamentoResponseDto(
        Integer id,
        LocalDateTime dataHora,
        StatusAgendamento status,
        Integer clienteId,
        Integer barbeiroId,
        Integer servicoId,
        Integer barbeariaId,
        LocalDateTime criadoEm
) {
    public static AgendamentoResponseDto fromEntity(Agendamento agendamento) {
        return new AgendamentoResponseDto(
                agendamento.getId(),
                agendamento.getDataHora(),
                agendamento.getStatus(),
                agendamento.getCliente() != null ? agendamento.getCliente().getId() : null,
                agendamento.getBarbeiro() != null ? agendamento.getBarbeiro().getId() : null,
                agendamento.getServico() != null ? agendamento.getServico().getId() : null,
                agendamento.getBarbearia() != null ? agendamento.getBarbearia().getId() : null,
                agendamento.getCriadoEm()
        );
    }
}
