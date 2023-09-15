package ru.gpm.example.flux.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
public class WebClientConfig {

    @Value("${app.externalHost}")
    private String extHost;

    @Bean
    public WebClient webClient() {
        return WebClient.builder().baseUrl(extHost).build();
    }
}
