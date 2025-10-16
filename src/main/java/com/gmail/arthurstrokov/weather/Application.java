package com.gmail.arthurstrokov.weather;

import com.gmail.arthurstrokov.weather.configuration.BotProperties;
import com.gmail.arthurstrokov.weather.configuration.OpenWeatherProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Arthur Strokov
 * @email arthurstrokov@gmail.com
 * @created 15.09.2022
 */
@EnableFeignClients
@SpringBootApplication
@EnableConfigurationProperties({BotProperties.class, OpenWeatherProperties.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
