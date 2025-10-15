package com.gmail.arthurstrokov.weather.startup;

import com.gmail.arthurstrokov.weather.service.WeatherForTomorrowBotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * @author Arthur Strokov
 * @email arthurstrokov@gmail.com
 * @created 24.09.2022
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationRunnerImpl implements ApplicationRunner {

    private final WeatherForTomorrowBotService weatherForTomorrowBotService;

    @Override
    public void run(ApplicationArguments args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(weatherForTomorrowBotService);
        } catch (TelegramApiException e) {
            log.error("Failed to register Telegram bot", e);
        }
    }
}
