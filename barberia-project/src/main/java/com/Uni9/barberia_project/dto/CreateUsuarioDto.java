package com.Uni9.barberia_project.dto;

import com.Uni9.barberia_project.model.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record CreateUsuarioDto(
        @NotBlank(message= "O nome é obrigatório.")
        @Length(max = 100, message= "O nome deve ter no máximo 100 caracteres.")
        String nome,

        @NotBlank(message= "O email é obrigatório")
        @Email(message= "O email deve ser válido")
        @Length(max = 100, message= "O email deve ter no máximo 100 caracteres.")
        String email,

        @NotBlank(message = "A senha é obrigatório")
        @Length(min= 4, max= 100, message= "A senha deve ter entre 4 a 100 caracteres.")
        String senha,

        @NotBlank(message= "O telefone é obrigatório.")
        @Pattern(regexp = "\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}",
                 message = "O telefone deve estar em um formato válido (ex: (XX) XXXX-XXXX ou XXXXXXXXX).")
        String telefone,

        @NotNull(message = "O tipo de usuário é obrigatório")
        Usuario.TipoUsuario tipo,

        Integer barbeariaId
) {}
