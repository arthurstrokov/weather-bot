package com.gmail.arthurstrokov.weather.service;

import com.gmail.arthurstrokov.weather.configuration.BotProperties;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;
import java.util.Locale;

@Slf4j
@Service
public class WeatherBotService extends TelegramLongPollingBot {

    private static final String COMMAND_START = "/start";
    private static final String COMMAND_FORECAST = "/forecast";
    private static final String COMMAND_CURRENT = "/current";
    private static final String COMMAND_LOCATION = "/location"; // псевдо-команда

    private static final String BUTTON_LOCATION_TEXT = "Share location";
    private static final String MENU_PROMPT = "Выберите действие:";
    private static final String HELP_TEXT = """
            Доступные команды:
            /current  — текущая погода
            /forecast — прогноз погоды
            /location — отправить геолокацию
            """;
    private static final String CITY = "Minsk";

    private final BotProperties botProperties;
    private final OpenWeatherService openWeatherService;
    private final ChatService chatService;

    public WeatherBotService(BotProperties botProperties,
                             OpenWeatherService openWeatherService,
                             ChatService chatService) {
        super(botProperties.getToken());
        this.botProperties = botProperties;
        this.openWeatherService = openWeatherService;
        this.chatService = chatService;
    }

    @PostConstruct
    public void initCommands() {
        try {
            List<BotCommand> commands = List.of(
                    new BotCommand(COMMAND_CURRENT, "Текущая погода"),
                    new BotCommand(COMMAND_FORECAST, "Прогноз погоды"),
                    new BotCommand(COMMAND_LOCATION, "Поделиться локацией")
            );
            execute(SetMyCommands.builder()
                    .commands(commands)
                    .scope(new BotCommandScopeDefault())
                    .languageCode(null)
                    .build()
            );
        } catch (Exception e) {
            log.error("Failed to set bot commands: {}", e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update == null) return;

        if (update.hasCallbackQuery()) {
            handleCallback(update.getCallbackQuery());
            return;
        }

        if (!update.hasMessage()) return;
        Message message = update.getMessage();
        long chatId = message.getChatId();

        if (message.hasLocation() && message.getLocation() != null) {
            handleLocation(chatId, message);
            return;
        }

        if (!message.hasText() || !StringUtils.hasText(message.getText())) {
            sendInlineMenu(chatId);
            return;
        }

        String text = message.getText().trim();
        switch (text.toLowerCase(Locale.ROOT)) {
            case COMMAND_START -> {
                String greeting = "Привет! Я бот погоды.\n\n" + HELP_TEXT;
                sendMsgWithInlineMenu(chatId, greeting);
            }
            case COMMAND_FORECAST -> sendMsgWithInlineMenu(chatId,
                    openWeatherService.getWeatherForecastByCity(CITY));
            case COMMAND_CURRENT -> sendMsgWithInlineMenu(chatId,
                    openWeatherService.getCurrentWeatherByCity(CITY));
            case COMMAND_LOCATION -> sendLocationRequestKeyboard(chatId, "Отправьте вашу геолокацию:");
            default -> {
                String weatherDescription = chatService.getWeatherForecastWithChat(text);
                sendMsgWithInlineMenu(chatId, weatherDescription);
            }
        }
    }

    private void handleLocation(long chatId, Message message) {
        double lat = message.getLocation().getLatitude();
        double lon = message.getLocation().getLongitude();
        String reply = openWeatherService.getWeatherForecastByGeographicCoordinates(lat, lon);
        sendMsgWithInlineMenu(chatId, reply);
    }

    private void handleCallback(CallbackQuery callback) {
        String data = callback.getData();
        long chatId = callback.getMessage().getChatId();

        safeAnswerCallback(callback.getId());

        switch (data) {
            case COMMAND_FORECAST -> sendMsgWithInlineMenu(chatId,
                    openWeatherService.getWeatherForecastByCity(CITY));
            case COMMAND_CURRENT -> sendMsgWithInlineMenu(chatId,
                    openWeatherService.getCurrentWeatherByCity(CITY));
            case COMMAND_LOCATION -> sendLocationRequestKeyboard(chatId,
                    "Нажмите кнопку ниже, чтобы поделиться локацией:");
            default -> sendMsgWithInlineMenu(chatId, "Неизвестная команда.");
        }
    }

    private void safeAnswerCallback(String callbackId) {
        try {
            AnswerCallbackQuery answer = AnswerCallbackQuery.builder()
                    .callbackQueryId(callbackId)
                    .text(null)
                    .showAlert(false)
                    .build();
            execute(answer);
        } catch (Exception e) {
            log.error("Error answering callback: {}", e.getMessage());
        }
    }

    private void sendMsgWithInlineMenu(long chatId, String text) {
        try {
            SendMessage msg = SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .build();
            msg.enableHtml(true);
            msg.setReplyMarkup(buildInlineMenu());
            execute(msg);
        } catch (Exception e) {
            log.error("Error sending message: {}", e.getMessage());
        }
    }

    private void sendInlineMenu(long chatId) {
        try {
            SendMessage msg = SendMessage.builder()
                    .chatId(chatId)
                    .text(WeatherBotService.MENU_PROMPT)
                    .build();
            msg.enableHtml(true);
            msg.setReplyMarkup(buildInlineMenu());
            execute(msg);
        } catch (Exception e) {
            log.error("Error sending inline menu: {}", e.getMessage());
        }
    }

    private void sendLocationRequestKeyboard(long chatId, String prompt) {
        try {
            ReplyKeyboardMarkup replyMarkup = new ReplyKeyboardMarkup();
            replyMarkup.setSelective(true);
            replyMarkup.setResizeKeyboard(true);
            replyMarkup.setOneTimeKeyboard(true);

            KeyboardButton locationButton = new KeyboardButton(BUTTON_LOCATION_TEXT);
            locationButton.setRequestLocation(true);

            KeyboardRow row = new KeyboardRow();
            row.add(locationButton);

            replyMarkup.setKeyboard(List.of(row));

            SendMessage msg = SendMessage.builder()
                    .chatId(chatId)
                    .text(prompt)
                    .replyMarkup(replyMarkup)
                    .build();
            msg.enableHtml(true);
            execute(msg);
        } catch (Exception e) {
            log.error("Error sending location keyboard: {}", e.getMessage());
        }
    }

    private InlineKeyboardMarkup buildInlineMenu() {
        InlineKeyboardButton forecastBtn = InlineKeyboardButton.builder()
                .text("🌤 Forecast")
                .callbackData(COMMAND_FORECAST)
                .build();

        InlineKeyboardButton currentBtn = InlineKeyboardButton.builder()
                .text("🌤 Current")
                .callbackData(COMMAND_CURRENT)
                .build();

        InlineKeyboardButton locationBtn = InlineKeyboardButton.builder()
                .text("📍 Share location")
                .callbackData(COMMAND_LOCATION)
                .build();

        List<List<InlineKeyboardButton>> rows = List.of(
                List.of(forecastBtn, currentBtn),
                List.of(locationBtn)
        );

        return new InlineKeyboardMarkup(rows);
    }

    @Override
    public String getBotUsername() {
        return botProperties.getName();
    }
}
