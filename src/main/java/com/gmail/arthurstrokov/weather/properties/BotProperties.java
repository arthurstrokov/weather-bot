package com.gmail.arthurstrokov.weather.properties;

import jakarta.validation.constraints.NotBlank;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "bot")
@Validated
public record BotProperties(
        String name,
        @NotBlank(message = "Bot token must not be blank") String token,
        String chatId) {

    @NotNull
    @Override
    public String toString() {
        return "BotProperties{name='%s', token='*****', chatId='%s'}".formatted(name, chatId);
    }
}
