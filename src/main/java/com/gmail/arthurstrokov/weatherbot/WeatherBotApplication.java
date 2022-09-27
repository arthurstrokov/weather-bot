package com.gmail.arthurstrokov.weatherbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Arthur Strokov
 * @email arthurstrokov@gmail.com
 * @created 15.09.2022
 */
@EnableFeignClients
@SpringBootApplication
public class WeatherBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherBotApplication.class, args);
    }
}
