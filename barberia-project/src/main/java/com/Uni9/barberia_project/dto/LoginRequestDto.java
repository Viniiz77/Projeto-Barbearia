package com.Uni9.barberia_project.dto;

import com.Uni9.barberia_project.model.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO para requisição de login de usuário (cliente ou barbeiro).")
public record LoginRequestDto(
        @NotBlank(message= "O e-mail é obrigatório.")
        @Email(message = "O email deve ser válido")
        @Schema(description = "Endereço de e-mail do usuário.", example = "joao.silva@email.com")
        String email,

        @NotBlank(message = "A senha é obrigatória.")
        @Schema(description = "Senha do usuário.", example = "senha123")
        String senha,

        Usuario.TipoUsuario tipo
) {
}
