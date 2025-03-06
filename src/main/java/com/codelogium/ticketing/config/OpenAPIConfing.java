package com.codelogium.ticketing.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfing {
    @Bean
    public OpenAPI defineOpenAPI() {
        Contact contact = new Contact();
        contact.setName("Al-Fahami Toihir");
        contact.setEmail("altoihir@gmail.com");
        contact.setUrl("https://www.linkedin.com/in/alfahami/");

        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("Server URL for local development");

        License license = new License().name("Creative Common License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info().title("IT Support Ticket System")
        .contact(contact)
        .version("1.0")
        .description("This API exposes endpoints to manage IT Support Tickets")
        .license(license);

        return new OpenAPI().info(info).servers(List.of(localServer));


    }
}
