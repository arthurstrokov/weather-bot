package com.gmail.arthurstrokov.weather.service;

import com.gmail.arthurstrokov.weather.configuration.BotProperties;
import com.gmail.arthurstrokov.weather.gateway.WeatherGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BotServiceTest {

    @Mock
    private BotProperties botProperties;

    @Mock
    private WeatherGateway weatherGateway;

    @Mock
    private WeatherService weatherService;

    private BotService botService;

    @BeforeEach
    void setUp() {
        when(botProperties.token()).thenReturn("test-token");
        when(botProperties.name()).thenReturn("TestBot");
        botService = new BotService(botProperties, weatherGateway, weatherService);
    }

    @Test
    @DisplayName("Should return bot token")
    void getBotToken() {
        // when
        String token = botService.getBotToken();
        // then
        assertThat(token).isEqualTo("test-token");
    }

    @Test
    @DisplayName("Should get updates consumer")
    void getUpdatesConsumer() {
        // when
        var consumer = botService.getUpdatesConsumer();
        // then
        assertThat(consumer)
                .isNotNull()
                .isSameAs(botService);
    }
}
