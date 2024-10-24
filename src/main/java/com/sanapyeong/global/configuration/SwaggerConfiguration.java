package com.sanapyeong.global.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@OpenAPIDefinition(
        info = @Info(title = "MTVS-3rd-DREAM-PLANET",
                description = "API statement for Dream Planet",
                version = "v1")
)
public class SwaggerConfiguration {
    private static final SecurityScheme apiKey = new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .in(SecurityScheme.In.HEADER)
            .name("Authorization")
            .scheme("bearer");
    private static final SecurityRequirement securityRequirement = new SecurityRequirement()
            .addList("Bearer Token");

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("Bearer Token", apiKey))
                .addSecurityItem(securityRequirement);
    }

    @Bean
    public GroupedOpenApi dev(){
        String[] path = {"/v1/dev/**"};

        return GroupedOpenApi
                .builder()
                .group("Dream Planet Swagger v1-dev")
                .pathsToMatch(path)
                .build();
    }
}
