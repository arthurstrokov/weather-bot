package com.gmail.arthurstrokov.weatherbot.service;

import com.gmail.arthurstrokov.weatherbot.configuration.BotProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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
        // Check if the update has a location
        Optional<Location> location = Optional.ofNullable(update.getMessage().getLocation());
        log.info(location.toString());
        // We check if the update has a message and the message has text
        if (update.hasMessage()) {
            long chatId = update.getMessage().getChatId();
            if (update.getMessage().hasLocation()) {
                Double longitude = update.getMessage().getLocation().getLongitude();
                Double latitude = update.getMessage().getLocation().getLatitude();
                String weatherForecastDataByGeographicCoordinates =
                        openWeatherApiService.getWeatherForecastDataByGeographicCoordinates(longitude, latitude);
                sendMsg(chatId, weatherForecastDataByGeographicCoordinates);

            } else if (update.getMessage().getText().equals("/start")) {
                String weatherForecastDataByCity = openWeatherApiService.getWeatherForecastDataByCity();
                sendMsg(chatId, weatherForecastDataByCity);

            } else if (update.getMessage().getText().equals("/test")) {
                String currentWeatherByCity = openWeatherApiService.getCurrentWeatherByCity();
                sendMsg(chatId, currentWeatherByCity);
            }
        }
    }

    private void sendMsg(long chatId, String text) {
        SendMessage sendMessage = SendMessage.builder() // Create a message object
                .chatId(chatId)
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

        KeyboardButton keyboardButton = new KeyboardButton();
        keyboardButton.setText("/loc");
        keyboardButton.setRequestLocation(true);

        keyboardFirstRow.add(keyboardButton);
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
