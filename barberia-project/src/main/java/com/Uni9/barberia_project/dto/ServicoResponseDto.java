package com.Uni9.barberia_project.dto;

import com.Uni9.barberia_project.model.Servico;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ServicoResponseDto(
        Integer id,
        String nome,
        String descricao,
        BigDecimal preco,
        Integer duracaoMinutos,
        Integer barbeariaId,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {
    public static ServicoResponseDto fromEntity(Servico servico) {
        return new ServicoResponseDto(
                servico.getId(),
                servico.getNome(),
                servico.getDescricao(),
                servico.getPreco(),
                servico.getDuracaoMinutos(),
                servico.getBarbearia() != null ? servico.getBarbearia().getId() : null,
                servico.getCriadoEm(),
                servico.getAtualizadoEm()
        );
    }
}
