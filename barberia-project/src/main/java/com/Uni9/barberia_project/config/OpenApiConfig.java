package com.Uni9.barberia_project.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Barbearia API",
                version = "1.0",
                description = "Documentação completa da API da barbearia para agendamento de serviços.",
                contact = @Contact(
                        name = "Grupo Uninove",
                        email = "vbrandao903@uni9.edu.br"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"
                )

        ),
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Servidor de Desenvolvimento local"
                )
        }
)
public class OpenApiConfig {
}
