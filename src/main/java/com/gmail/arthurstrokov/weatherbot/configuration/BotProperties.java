package com.gmail.arthurstrokov.weatherbot.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

/**
 * @author Arthur Strokov
 * @email arthurstrokov@gmail.com
 * @created 24.09.2022
 */
@Configuration
@PropertySource("classpath:values.properties")
public class BotProperties {

    private final Environment environment;
    private String botName;
    private String token;
    private String chatId;

    @Autowired
    public BotProperties(Environment environment) {
        this.environment = environment;
    }

    /**
     * Spring calls the methods annotated with @PostConstruct only once,
     * just after the initialization of bean properties.
     */
    @PostConstruct
    public void initialize() {
        this.botName = environment.getProperty("bot.name");
        this.token = environment.getProperty("bot.token");
        this.chatId = environment.getProperty("bot.chatId");
    }

    public String getBotName() {
        return botName;
    }

    public String getToken() {
        return token;
    }

    public String getChatId() {
        return chatId;
    }
}
