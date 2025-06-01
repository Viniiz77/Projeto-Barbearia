package com.Uni9.barberia_project.dto;

import com.Uni9.barberia_project.model.Barbearia;
import com.Uni9.barberia_project.model.Servico;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record BarbeariaResponseDto(
        Integer id,
        String nome,
        String endereco,
        String telefone,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm,
        List<Servico> servicos
) {
    public static BarbeariaResponseDto fromEntity(Barbearia barbearia) {
        List<Servico> servicosCarregados = barbearia.getServicos() != null ? barbearia.getServicos() :
                                           java.util.Collections.emptyList();

        return new BarbeariaResponseDto(
                barbearia.getId(),
                barbearia.getNome(),
                barbearia.getEndereco(),
                barbearia.getTelefone(),
                barbearia.getCriadoEm(),
                barbearia.getAtualizadoEm(),
                servicosCarregados
        );
    }
}
