package com.test.forleven.infra.config.utils;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Restful API")
                        .description("API Springboot RESTFUL - " +
                                "Responsável por cadastro de alunos com emissão de matricula.")
                        .contact(new Contact()
                                .name("Reynaldo Hendson")
                                .email("reynaldohendsondev@outlook.com")
                        )
                );
    }

}
