package com.Uni9.barberia_project.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateServicoDto(
        @Size(max = 100, message = "O nome do serviço não pode ter mais de 100 caracteres.")
        String nome,

        String descricao,

        @DecimalMin(value= "0.01",message= "O preço deve ser maior que zero")
        BigDecimal preco
) {}
