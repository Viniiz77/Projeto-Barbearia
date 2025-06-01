package com.Uni9.barberia_project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CreateServicoDto(
        @NotBlank(message= "O nome do serviço é obrigatório")
        String nome,

        String descricao,

        @DecimalMin(value= "0.01", message= "O preço deve ser maior que zero")
        BigDecimal preco,

        @NotNull(message= "O id da barbearia é obrigatório")
        Integer barbeariaId
) {}
