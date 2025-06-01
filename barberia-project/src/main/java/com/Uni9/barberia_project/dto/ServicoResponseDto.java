package com.Uni9.barberia_project.dto;

import com.Uni9.barberia_project.model.Servico;

import java.math.BigDecimal;

public record ServicoResponseDto(
        Integer id,
        String nome,
        String descricao,
        BigDecimal preco,
        Integer barbeariaId
) {
    public static ServicoResponseDto fromEntity(Servico servico) {
        return new ServicoResponseDto(
                servico.getId(),
                servico.getNome(),
                servico.getDescricao(),
                servico.getPreco(),
                servico.getBarbearia() != null ? servico.getBarbearia().getId() : null
        );
    }
}
