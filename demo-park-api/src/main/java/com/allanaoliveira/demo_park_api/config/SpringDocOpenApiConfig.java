package com.allanaoliveira.demo_park_api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocOpenApiConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI().components(new Components().addSecuritySchemes("Security Token", securityScheme()))
                .info(new Info().title("REST API - Spring Park")
                        .description("API Para Gestão de Estacionamento de veiculos")
                        .version("1.0")
                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0.html"))
                        .contact(new Contact().name("Allana").email("Larissaleonel@gmail.com"))
                );
    }
    private SecurityScheme securityScheme(){
        return new SecurityScheme()
                .description("Insira um Token válido para prosseguir")
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .scheme("bearer")
                .bearerFormat("JWT")
                .name("Security Token");
    }
}
