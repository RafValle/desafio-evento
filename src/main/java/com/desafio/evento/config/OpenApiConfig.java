package com.desafio.evento.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "API de Gerenciamento de Eventos", description = "Altere e descreva o objetivo do seu serviço", version = "1.0.0"))
public class OpenApiConfig {

}
