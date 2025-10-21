package com.gmail.arthurstrokov.weather.service;

import com.gmail.arthurstrokov.weather.configuration.BotProperties;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;
import java.util.Locale;

@Slf4j
@Service
public class WeatherBotService implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private static final String COMMAND_START = "/start";
    private static final String COMMAND_FORECAST = "/forecast";
    private static final String COMMAND_CURRENT = "/current";
    private static final String COMMAND_LOCATION = "/location";

    private static final String BUTTON_LOCATION_TEXT = "Share location";
    private static final String MENU_PROMPT = "Выберите действие:";
    private static final String HELP_TEXT = """
            Доступные команды:
            /current  — текущая погода
            /forecast — прогноз погоды
            /location — отправить геолокацию
            """;
    private static final String DEFAULT_CITY = "Minsk";

    private final BotProperties botProperties;
    private final WeatherService weatherService;
    private final ChatService chatService;
    private final TelegramClient telegramClient;

    public WeatherBotService(BotProperties botProperties,
                             WeatherService weatherService,
                             ChatService chatService) {
        this.botProperties = botProperties;
        this.weatherService = weatherService;
        this.chatService = chatService;
        this.telegramClient = new OkHttpTelegramClient(getBotToken());
    }

    @PostConstruct
    public void initCommands() {
        try {
            List<BotCommand> commands = List.of(
                    new BotCommand(COMMAND_CURRENT, "Текущая погода"),
                    new BotCommand(COMMAND_FORECAST, "Прогноз погоды"),
                    new BotCommand(COMMAND_LOCATION, "Поделиться локацией")
            );
            telegramClient.execute(SetMyCommands.builder()
                    .commands(commands)
                    .scope(new BotCommandScopeDefault())
                    .languageCode(null)
                    .build()
            );
        } catch (Exception e) {
            log.error("Failed to set bot commands", e);
        }
    }

    // --- SpringLongPollingBot contract ---

    @Override
    public String getBotToken() {
        return botProperties.token();
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this; // single-thread consumer
    }

    // --- Single-thread consumer entrypoint ---

    @Override
    public void consume(Update update) {
        try {
            handleUpdate(update);
        } catch (Exception e) {
            log.error("Error while handling update", e);
        }
    }

    // --- Update handling ---

    private void handleUpdate(Update update) {
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
                    weatherService.getWeatherForecastByCity(DEFAULT_CITY));
            case COMMAND_CURRENT -> sendMsgWithInlineMenu(chatId,
                    weatherService.getCurrentWeatherByCity(DEFAULT_CITY));
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
        String reply = weatherService.getWeatherForecastByGeographicCoordinates(lat, lon);
        sendMsgWithInlineMenu(chatId, reply);
    }

    private void handleCallback(CallbackQuery callback) {
        String data = callback.getData();
        long chatId = callback.getMessage().getChatId();

        safeAnswerCallback(callback.getId());

        switch (data) {
            case COMMAND_FORECAST -> sendMsgWithInlineMenu(chatId,
                    weatherService.getWeatherForecastByCity(DEFAULT_CITY));
            case COMMAND_CURRENT -> sendMsgWithInlineMenu(chatId,
                    weatherService.getCurrentWeatherByCity(DEFAULT_CITY));
            case COMMAND_LOCATION -> sendLocationRequestKeyboard(chatId,
                    "Нажмите кнопку ниже, чтобы поделиться локацией:");
            default -> sendMsgWithInlineMenu(chatId, "Неизвестная команда.");
        }
    }

    // --- Telegram API helpers ---

    private void safeAnswerCallback(String callbackId) {
        try {
            telegramClient.execute(AnswerCallbackQuery.builder()
                    .callbackQueryId(callbackId)
                    .showAlert(false)
                    .build());
        } catch (Exception e) {
            log.error("Error answering callback", e);
        }
    }

    private void sendMsgWithInlineMenu(long chatId, String text) {
        try {
            SendMessage msg = SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .replyMarkup(buildInlineMenu())
                    .build();
            msg.enableHtml(true);
            telegramClient.execute(msg);
        } catch (Exception e) {
            log.error("Error sending message", e);
        }
    }

    private void sendInlineMenu(long chatId) {
        try {
            SendMessage msg = SendMessage.builder()
                    .chatId(chatId)
                    .text(MENU_PROMPT)
                    .replyMarkup(buildInlineMenu())
                    .build();
            msg.enableHtml(true);
            telegramClient.execute(msg);
        } catch (Exception e) {
            log.error("Error sending inline menu", e);
        }
    }

    private void sendLocationRequestKeyboard(long chatId, String prompt) {
        try {
            ReplyKeyboardMarkup replyMarkup = ReplyKeyboardMarkup.builder()
                    .selective(true)
                    .resizeKeyboard(true)
                    .oneTimeKeyboard(true)
                    .build();

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
            telegramClient.execute(msg);
        } catch (Exception e) {
            log.error("Error sending location keyboard", e);
        }
    }

    // --- Inline keyboard builder (v7 API) ---

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

        InlineKeyboardRow row1 = new InlineKeyboardRow();
        row1.add(forecastBtn);
        row1.add(currentBtn);

        InlineKeyboardRow row2 = new InlineKeyboardRow();
        row2.add(locationBtn);

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(row1, row2))
                .build();
    }
}
