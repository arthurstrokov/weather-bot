package com.gmail.arthurstrokov.weather.configuration;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OllamaFeignConfig {

    @Bean
    public RequestInterceptor authHeader() {
        // Берём API-ключ из переменной окружения OLLAMA_API_KEY
        String apiKey = System.getenv("OLLAMA_API_KEY");
        return template -> {
            if (apiKey != null && !apiKey.isBlank()) {
                template.header("Authorization", "Bearer " + apiKey);
            }
            template.header("Content-Type", "application/json");
        };
    }
}
