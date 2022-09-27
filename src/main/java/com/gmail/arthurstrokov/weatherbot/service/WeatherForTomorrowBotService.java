package com.gmail.arthurstrokov.weatherbot.service;

import com.gmail.arthurstrokov.weatherbot.configuration.BotProperties;
import com.gmail.arthurstrokov.weatherbot.configuration.OpenApiProperties;
import com.gmail.arthurstrokov.weatherbot.gateway.OpenWeatherApiClient;
import lombok.RequiredArgsConstructor;
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
    private final OpenWeatherApiClient openWeatherApiClient;
    private final OpenApiProperties openApiProperties;

    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage()) {
            long chatId = update.getMessage().getChatId();
            // Check if the update has a location
            if (update.getMessage().hasLocation()) {
                Double latitude = update.getMessage().getLocation().getLatitude();
                Double longitude = update.getMessage().getLocation().getLongitude();
                String currentWeatherByGeographicCoordinates =
                        openWeatherApiService.getCurrentWeatherByGeographicCoordinates(latitude, longitude);
                sendMsg(chatId, currentWeatherByGeographicCoordinates);

            } else if (update.getMessage().getText().equals("/start")) {
                String weatherForecastDataByCity = openWeatherApiService.getWeatherForecastDataByCity();
                sendMsg(chatId, weatherForecastDataByCity);

            } else if (update.getMessage().getText().equals("/test")) {
                String currentWeatherByCity = openWeatherApiClient.getCurrentWeatherByCity(openApiProperties.getCityName(), openApiProperties.getOpenApiKey());
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

        KeyboardButton keyboardButtonLoc = new KeyboardButton();
        keyboardButtonLoc.setText("/loc");
        keyboardButtonLoc.setRequestLocation(true);

        KeyboardButton keyboardButtonCur = new KeyboardButton();
        keyboardButtonCur.setText("/cur");
        keyboardButtonCur.setRequestLocation(true);

//        keyboardFirstRow.add(keyboardButtonLoc);TODO Register the button
        keyboardFirstRow.add(keyboardButtonCur);
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
