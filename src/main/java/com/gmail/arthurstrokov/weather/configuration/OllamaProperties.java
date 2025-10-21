package com.gmail.arthurstrokov.weather.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "chat")
public record OllamaProperties(String implementation) {
}
