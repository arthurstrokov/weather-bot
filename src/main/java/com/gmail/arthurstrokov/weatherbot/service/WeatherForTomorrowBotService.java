package com.gmail.arthurstrokov.weatherbot.service;

import com.gmail.arthurstrokov.weatherbot.configuration.BotProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


/**
 * @author Arthur Strokov
 * @email arthurstrokov@gmail.com
 * @created 15.09.2022
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherForTomorrowBotService extends TelegramLongPollingBot {

    private final BotProperties botProperties;
    private final OpenWeatherApiService openWeatherApiService;

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            if (update.getMessage().getText().equals("/start")) {
                openWeatherApiService.getWeather();
                sendMsg(chatId);
            } else {
                // TODO
            }
        }
    }

    private void sendMsg(Long chatId) {
        SendMessage message = SendMessage.builder() // Create a message object
                .chatId(chatId)
                .text("You send /start")
                .build();
        try {
            execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        // Return bot name
        return botProperties.getBotName();
    }

    @Override
    public String getBotToken() {
        // Return bot token from BotFather
        return botProperties.getToken();
    }
}
