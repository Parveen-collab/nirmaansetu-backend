package com.nirmaansetu.backend.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//This class is Swagger / OpenAPI configuration for the backend. It defines:
//API documentation details (title, version, contact)
//Security configuration (JWT authentication in Swagger UI)
@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {

    @Bean
    public OpenAPI nirmaanSetuOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("NirmaanSetu API")
                        .description("Backend API for NirmaanSetu - A Platform for connecting all aspects of Construction Sector.")
                        .version("v0.0.1")
                        .contact(new Contact()
                                .name("NirmaanSetu Team")
                                .email("support@nirmaansetu.com")));
    }
}
