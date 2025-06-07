package com.Uni9.barberia_project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

public record CreateServicoDto(
        @NotBlank(message= "O nome do serviço é obrigatório")
        String nome,

        String descricao,

        @NotNull(message = "O preço é obrigatório.")
        @DecimalMin(value= "0.01", message= "O preço deve ser maior que zero")
        BigDecimal preco,

        @NotNull(message = "A duração do serviço é obrigatória.")
        @Min(value = 5, message = "A duração mínima do serviço é de 5 minutos.") // Exemplo de validação
        Integer duracaoMinutos,

        @NotNull(message= "O id da barbearia é obrigatório")
        Integer barbeariaId
) {}
