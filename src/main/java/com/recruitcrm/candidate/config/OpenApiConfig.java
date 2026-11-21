package com.recruitcrm.candidate.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI candidateManagementOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Recruit CRM Candidate Management API")
                .description("APIs for creating, searching, updating, and deleting candidates")
                .version("1.0.0")
                .contact(new Contact()
                        .name("Recruit CRM")
                        .email("support@recruitcrm.com")));
    }
}

