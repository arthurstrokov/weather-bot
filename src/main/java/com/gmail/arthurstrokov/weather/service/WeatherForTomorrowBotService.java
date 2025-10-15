package com.gmail.arthurstrokov.weather.service;

import com.gmail.arthurstrokov.weather.aspect.NoLogging;
import com.gmail.arthurstrokov.weather.configuration.BotProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Locale;


/**
 * @author Arthur Strokov
 * @email arthurstrokov@gmail.com
 * @created 15.09.2022
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherForTomorrowBotService extends TelegramLongPollingBot {

    private static final String COMMAND_FORECAST = "/forecast";
    private static final String COMMAND_CURRENT = "/current";
    private static final String COMMAND_TEST = "/test";
    private static final String BUTTON_LOCATION = "Share location";

    private final BotProperties botProperties;
    private final PrintService printService;

    @Override
    public void onUpdateReceived(Update update) {
        if (update == null || !update.hasMessage()) {
            log.debug("Skipping update without message payload");
            return;
        }

        Message message = update.getMessage();
        long chatId = message.getChatId();

        if (message.hasLocation()) {
            handleLocation(chatId, message);
            return;
        }

        if (!message.hasText() || !StringUtils.hasText(message.getText())) {
            log.debug("Skipping message without text for chat {}", chatId);
            return;
        }

        String text = message.getText().trim();
        switch (text.toLowerCase(Locale.ROOT)) {
            case COMMAND_FORECAST -> sendMsg(chatId, printService.printWeatherForecast());
            case COMMAND_CURRENT, COMMAND_TEST -> sendMsg(chatId, printService.printCurrentWeather());
            default -> handleCityForecast(chatId, text);
        }
    }

    private void handleLocation(long chatId, Message message) {
        if (message.getLocation() == null) {
            log.debug("Location requested but payload is null for chat {}", chatId);
            return;
        }
        double latitude = message.getLocation().getLatitude();
        double longitude = message.getLocation().getLongitude();
        sendMsg(chatId, printService.printWeatherForecast(latitude, longitude));
    }

    private void handleCityForecast(long chatId, String city) {
        try {
            sendMsg(chatId, printService.printWeatherForecast(city));
        } catch (Exception ex) {
            log.warn("Failed to retrieve forecast for city '{}'", city, ex);
            sendMsg(chatId, "Unable to retrieve forecast for '" + city + "'. Try again later.");
        }
    }

    private void sendMsg(long chatId, String text) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        configureKeyboard(sendMessage);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Failed to send message to chat {}", chatId, e);
        }
    }

    private void configureKeyboard(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.enableHtml(true);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        KeyboardRow keyboardFirstRow = new KeyboardRow();

        KeyboardButton locationButton = new KeyboardButton(BUTTON_LOCATION);
        locationButton.setRequestLocation(true);

        keyboardFirstRow.add(locationButton);
        keyboardFirstRow.add(new KeyboardButton(COMMAND_FORECAST));
        keyboardFirstRow.add(new KeyboardButton(COMMAND_CURRENT));

        replyKeyboardMarkup.setKeyboard(List.of(keyboardFirstRow));
    }

    @Override
    public String getBotUsername() {
        // Return bot name
        return botProperties.getName();
    }

    @NoLogging
    @Override
    public String getBotToken() {
        // Return bot token from BotFather
        return botProperties.getToken();
    }
}
