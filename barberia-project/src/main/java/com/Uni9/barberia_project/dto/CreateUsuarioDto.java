package com.Uni9.barberia_project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.Uni9.barberia_project.model.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

@Schema(description = "DTO para criar um novo usuário no sistema.")
public record CreateUsuarioDto(
        @NotBlank(message= "O nome é obrigatório.")
        @Length(max = 100, message= "O nome deve ter no máximo 100 caracteres.")
        @Schema(description = "Nome completo do usuário.", example = "João da Silva")
        String nome,

        @NotBlank(message= "O email é obrigatório")
        @Email(message= "O email deve ser válido")
        @Length(max = 100, message= "O email deve ter no máximo 100 caracteres.")
        @Schema(description = "Endereço de e-mail do usuário, deve ser único.", example = "joao.silva@email.com")
        String email,

        @NotBlank(message = "A senha é obrigatório")
        @Length(min= 4, max= 100, message= "A senha deve ter entre 4 a 100 caracteres.")
        @Schema(description = "Senha do usuário. Deve ser segura.", example = "senha123")
        String senha,

        @NotBlank(message= "O telefone é obrigatório.")
        @Pattern(regexp = "\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}",
                 message = "O telefone deve estar em um formato válido (ex: (XX) XXXX-XXXX ou XXXXXXXXX).")
        @Schema(description = "Número de telefone do usuário. Formato livre.", example = "11987654321")
        String telefone,

        @NotNull(message = "O tipo de usuário é obrigatório")
        @Schema(description = "Tipo de usuário (CLIENTE, BARBEIRO).", example = "CLIENTE")
        Usuario.TipoUsuario tipo,

        @Schema(description = "ID da barbearia à qual o usuário está associado (obrigatório para BARBEIRO).", example = "1")
        Integer barbeariaId
) {}
