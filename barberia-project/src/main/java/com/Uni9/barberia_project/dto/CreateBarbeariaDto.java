package com.Uni9.barberia_project.dto;

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
        String telefone
) {}
