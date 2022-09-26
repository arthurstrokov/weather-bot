package com.gmail.arthurstrokov.weatherbot.service;

import com.gmail.arthurstrokov.weatherbot.configuration.BotProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;


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
        Message message = update.getMessage();
        if (update.hasMessage() && message.hasText()) {
            if (message.getText().equals("/start")) {
                String weatherForecastData = openWeatherApiService.getWeatherForecastData();
                sendMsg(message, weatherForecastData);
            } else if (update.getMessage().getText().equals("/test")) {
                String currentWeather = openWeatherApiService.getCurrentWeather();
                sendMsg(message, currentWeather);
            }
        }
    }

    private void sendMsg(Message message, String text) {
        SendMessage sendMessage = SendMessage.builder() // Create a message object
                .chatId(message.getChatId())
                .text(text)
                .build();
        try {
            setButton(sendMessage);
            execute(sendMessage); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void setButton(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.enableHtml(true);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("/start"));
        keyboardFirstRow.add(new KeyboardButton("/test"));
        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    @Override
    public String getBotUsername() {
        // Return bot name
        return botProperties.getBotName();
    }

    @Override
    public String getBotToken() {
        // Return bot token from BotFather
        return botProperties.getBotToken();
    }
}
