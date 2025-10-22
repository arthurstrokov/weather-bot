package com.gmail.arthurstrokov.weather.configuration;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class OllamaFeignConfig {

    private static final String AUTH_HEADER = "Authorization";

    @Value("${ollama.api-key}")
    private String apiKey;

    @Bean
    @ConditionalOnProperty(name = "chat.implementation", havingValue = "remote")
    public RequestInterceptor authHeader() {
        return template -> {
            if (StringUtils.hasText(apiKey)) {
                template.header(AUTH_HEADER, "Bearer " + apiKey);
            }
        };
    }
}
