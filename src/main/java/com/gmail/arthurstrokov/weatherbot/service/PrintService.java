package com.gmail.arthurstrokov.weatherbot.service;

import com.gmail.arthurstrokov.weatherbot.dto.List;
import com.gmail.arthurstrokov.weatherbot.dto.Weather;
import com.gmail.arthurstrokov.weatherbot.dto.WeatherForecastDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * @author Arthur Strokov
 * @email arthurstrokov@gmail.com
 * @created 27.09.2022
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PrintService {

    public static String formatMessage(WeatherForecastDto weatherForecastDto) {
        ArrayList<List> list = weatherForecastDto.getList();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n").append("city: ").append(weatherForecastDto.getCity().getName()).append("\n");
        list.forEach(x -> stringBuilder
                .append("---").append("\n")
                .append("Temp: ").append(x.getMain().getTemp()).append(" ")
                .append("Feels_like: ").append(x.getMain().getFeels_like()).append("\n")
                .append(x.getWeather().stream().map(Weather::getMain).collect(Collectors.toList())).append(": ")
                .append(x.getWeather().stream().map(Weather::getDescription).collect(Collectors.toList())).append("\n")
                .append(x.getClouds()).append("\n")
                .append("Rain: ").append(x.getRain()).append("\n")
                .append(x.getDt_txt()).append("\n")
        );
        return stringBuilder.toString();
    }
}
