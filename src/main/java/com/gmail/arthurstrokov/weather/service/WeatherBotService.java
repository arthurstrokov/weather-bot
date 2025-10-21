package com.gmail.arthurstrokov.weather.service;

import com.gmail.arthurstrokov.weather.configuration.BotProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class WeatherBotService extends TelegramLongPollingBot {

    private static final String COMMAND_FORECAST = "/forecast";
    private static final String COMMAND_CURRENT = "/current";
    private static final String COMMAND_TEST = "/test";
    private static final String BUTTON_LOCATION = "Share location";

    private final BotProperties botProperties;
    private final OpenWeatherService openWeatherService;

    @Override
    public void onUpdateReceived(Update update) {
        if (update == null || !update.hasMessage()) {
            return;
        }
        Message message = update.getMessage();
        long chatId = message.getChatId();
        if (message.hasLocation()) {
            handleLocation(chatId, message);
            return;
        }
        if (!message.hasText() || !StringUtils.hasText(message.getText())) {
            return;
        }
        String text = message.getText().trim();
        switch (text.toLowerCase(Locale.ROOT)) {
            case COMMAND_FORECAST -> sendMsg(chatId, openWeatherService.getWeatherForecastByCity());
            case COMMAND_CURRENT, COMMAND_TEST -> sendMsg(chatId, openWeatherService.getCurrentWeatherByCity());
            default -> handleWeatherForecastWithChat(chatId, text);
        }
    }

    private void handleLocation(long chatId, Message message) {
        if (message.getLocation() == null) {
            return;
        }
        double latitude = message.getLocation().getLatitude();
        double longitude = message.getLocation().getLongitude();
        sendMsg(chatId, openWeatherService.getWeatherForecastByGeographicCoordinates(latitude, longitude));
    }

    private void handleWeatherForecastWithChat(long chatId, String query) {
        String weatherDescription = openWeatherService.getWeatherForecastWithChat(query);
        sendMsg(chatId, weatherDescription);
    }

    @SneakyThrows
    private void sendMsg(long chatId, String text) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        configureKeyboard(sendMessage);
        execute(sendMessage);
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

    @Override
    public String getBotToken() {
        // Return bot token from BotFather
        return botProperties.getToken();
    }
}
