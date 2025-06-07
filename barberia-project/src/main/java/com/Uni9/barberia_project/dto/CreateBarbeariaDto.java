package com.Uni9.barberia_project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
public record CreateBarbeariaDto(
        @NotBlank (message= "O nome da barbearia é obrigatório.")
        @Size(max= 100, message= "O nome da barbearia não pode ter mais que 100 carcateres.")
        String nome,

        @NotBlank (message= "O endereço é obrigatório.")
        @Size(max= 255, message= "O endereço não pode ter mais que 255 carcateres.")
        String endereco,

        @NotBlank(message= "O telefone é obrigatório.")
        @Pattern(regexp = "^\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}$",
        message= "O telefone deve estar em um formato válido (ex: (XX) XXXX-XXXX ou XXXXXXXXX)")
        String telefone,

        @Size(max = 1000, message = "A descrição não pode ter mais que 1000 caracteres.")
        @Schema(description = "Descrição da barbearia.", example = "Uma barbearia moderna e acolhedora...")
        String descricao,

        @NotNull(message = "O ID do usuário (barbeiro) é obrigatório para cadastrar a barbearia.")
        Integer usuarioId
) {}
