package com.sr.authboilerplate.global.config.dev;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        String securityRequirementName = "bearerAuth";

        return new OpenAPI()
                .components(new Components().addSecuritySchemes(securityRequirementName, createAPIKeyScheme()))
                .info(new Info().title("Auth Boilerplate API")
                        .description("Auth Boilerplate API Swagger")
                        .version("0.1.0"))
                .externalDocs(new ExternalDocumentation().description("auth-boilerplate-api"));

    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(In.HEADER)
                .name("Authorization");
    }

}
