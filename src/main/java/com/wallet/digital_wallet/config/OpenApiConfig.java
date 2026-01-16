package com.wallet.digital_wallet.config;

import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger configuration for the Digital Wallet System.
 *
 * <p>This config provides high-level API metadata exposed via:
 * <ul>
 *   <li>/v3/api-docs</li>
 *   <li>/swagger-ui/index.html</li>
 * </ul>
 */

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