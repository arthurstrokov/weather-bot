package com.gmail.arthurstrokov.weatherbot.service;

import com.gmail.arthurstrokov.weatherbot.aspect.NoLogging;
import com.gmail.arthurstrokov.weatherbot.configuration.BotProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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
    private final PrintService printService;

    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage()) {
            long chatId = update.getMessage().getChatId();
            // Check if the update has a location
            if (update.getMessage().hasLocation()) {
                Double latitude = update.getMessage().getLocation().getLatitude();
                Double longitude = update.getMessage().getLocation().getLongitude();
                String weatherForecast = printService.printWeatherForecast(String.valueOf(latitude), String.valueOf(longitude));
                sendMsg(chatId, weatherForecast);

            } else if (update.getMessage().getText().equals("/forecast")) {
                String weatherForecast = printService.printWeatherForecast();
                sendMsg(chatId, weatherForecast);

            } else if (update.getMessage().getText().equals("/test")) {
                String currentWeather = printService.printCurrentWeather();
                sendMsg(chatId, currentWeather);

            } else {
                try {
                    String city = update.getMessage().getText();
                    String weatherForecastByCity = printService.printWeatherForecast(city);
                    sendMsg(chatId, weatherForecastByCity);
                } catch (Exception e) {
                    log.info(e.getMessage());
                }
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
        keyboardButtonLoc.setText("/location");
        keyboardButtonLoc.setRequestLocation(true);

        KeyboardButton keyboardButtonCur = new KeyboardButton(); // TODO Register the button
        keyboardButtonCur.setText("/current");
        keyboardButtonCur.setRequestLocation(true);

        keyboardFirstRow.add(keyboardButtonLoc);
        keyboardFirstRow.add(new KeyboardButton("/forecast"));
        keyboardFirstRow.add(new KeyboardButton("/test"));

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    @Override
    public String getBotUsername() {
        // Return bot name
        return botProperties.getBotName();
    }

    @NoLogging
    @Override
    public String getBotToken() {
        // Return bot token from BotFather
        return botProperties.getBotToken();
    }
}
