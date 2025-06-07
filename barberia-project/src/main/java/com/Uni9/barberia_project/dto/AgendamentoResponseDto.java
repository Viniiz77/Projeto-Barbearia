package com.Uni9.barberia_project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.Uni9.barberia_project.model.Agendamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "DTO de resposta contendo os detalhes de um agendamento.")
public record AgendamentoResponseDto(
        @Schema(description = "ID único do agendamento.", example = "123")
        Integer id,

        @Schema(description = "Data e hora do agendamento. Formato: AAAA-MM-DDTHH:mm:ss", example = "2025-07-20T10:30:00")
        LocalDateTime dataHora,

        @Schema(description = "Status atual do agendamento (PENDENTE, CONFIRMADO, CANCELADO, CONCLUIDO).", example = "PENDENTE")
        String status,

        @Schema(description = "ID do cliente que fez o agendamento.", example = "1")
        Integer clienteId,
        @Schema(description = "Nome do cliente que fez o agendamento.", example = "João Silva")
        String clienteNome,

        @Schema(description = "ID do barbeiro designado para o agendamento.", example = "2")
        Integer barbeiroId,
        @Schema(description = "Nome do barbeiro designado para o agendamento.", example = "Zé da Navalha")
        String barbeiroNome,

        @Schema(description = "ID do serviço agendado.", example = "1")
        Integer servicoId,
        @Schema(description = "Nome do serviço agendado.", example = "Corte de Cabelo")
        String servicoNome,
        @Schema(description = "Preço do serviço agendado.", example = "50.00")
        BigDecimal servicoPreco,
        @Schema(description = "Duração em minutos do serviço agendado.", example = "30")
        Integer servicoDuracaoMinutos,

        @Schema(description = "ID da barbearia onde o agendamento foi feito.", example = "1")
        Integer barbeariaId,
        @Schema(description = "Nome da barbearia onde o agendamento foi feito.", example = "Barbearia do Zé")
        String barbeariaNome,

        @Schema(description = "Data e hora de criação do agendamento.", example = "2025-06-01T17:00:00")
        LocalDateTime criadoEm
) {
    public static AgendamentoResponseDto fromEntity(Agendamento agendamento) {
        return new AgendamentoResponseDto(
                agendamento.getId(),
                agendamento.getDataHora(),
                agendamento.getStatus().name(),
                agendamento.getCliente() != null ? agendamento.getCliente().getId() : null,
                agendamento.getCliente() != null ? agendamento.getCliente().getNome() : null, // Obtém nome do cliente

                agendamento.getBarbeiro() != null ? agendamento.getBarbeiro().getId() : null,
                agendamento.getBarbeiro() != null ? agendamento.getBarbeiro().getNome() : null, // Obtém nome do barbeiro

                agendamento.getServico() != null ? agendamento.getServico().getId() : null,
                agendamento.getServico() != null ? agendamento.getServico().getNome() : null, // Obtém nome do serviço
                agendamento.getServico() != null ? agendamento.getServico().getPreco() : null, // Obtém preço do serviço
                agendamento.getServico() != null ? agendamento.getServico().getDuracaoMinutos() : null, // Obtém duração do serviço

                agendamento.getBarbearia() != null ? agendamento.getBarbearia().getId() : null,
                agendamento.getBarbearia() != null ? agendamento.getBarbearia().getNome() : null, // Obtém nome da barbearia

                agendamento.getCriadoEm()
        );
    }
}
