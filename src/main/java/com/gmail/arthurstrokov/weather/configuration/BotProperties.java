package com.gmail.arthurstrokov.weather.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Arthur Strokov
 * @email arthurstrokov@gmail.com
 * @created 24.09.2022
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "bot")
public class BotProperties {
    /**
     * Public bot name registered in BotFather.
     */
    private String name;

    /**
     * Token issued by BotFather; never commit a real value.
     */
    private String token;

    /**
     * Optional chat id used for diagnostics.
     */
    private String chatId;
}
