package com.wallet.digital_wallet.config;

import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Digital Wallet API")
                        .version("1.0")
                        .description("RESTful API for Digital Wallet System")
                        .contact(new Contact()
                                .name("Joshua Maina")
                                .email("dev.joshuamaina@gmail.com")))
                .servers(List.of(new Server().url("http://localhost:8080").description("Local Server")));
    }
}