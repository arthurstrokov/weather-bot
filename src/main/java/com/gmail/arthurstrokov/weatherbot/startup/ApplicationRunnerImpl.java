package com.gmail.arthurstrokov.weatherbot.startup;

import com.gmail.arthurstrokov.weatherbot.configuration.BotProperties;
import com.gmail.arthurstrokov.weatherbot.configuration.OpenApiProperties;
import com.gmail.arthurstrokov.weatherbot.service.OpenWeatherApiService;
import com.gmail.arthurstrokov.weatherbot.service.WeatherForTomorrowBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * @author Arthur Strokov
 * @email arthurstrokov@gmail.com
 * @created 24.09.2022
 */
@Component
@RequiredArgsConstructor
public class ApplicationRunnerImpl implements ApplicationRunner {

    private final BotProperties botProperties;
    private final OpenApiProperties openApiProperties;
    private final OpenWeatherApiService openWeatherApiService;

    @Override
    public void run(ApplicationArguments args) {
        try {
            // Create the TelegramBotsApi object to register your bots
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            // Register your newly created AbilityBot
            botsApi.registerBot(new WeatherForTomorrowBotService(
                    botProperties,
                    openApiProperties,
                    openWeatherApiService
            ));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
