package com.gmail.arthurstrokov.weatherbot.service;

import com.gmail.arthurstrokov.weatherbot.configuration.OpenApiProperties;
import com.gmail.arthurstrokov.weatherbot.dto.List;
import com.gmail.arthurstrokov.weatherbot.dto.Weather;
import com.gmail.arthurstrokov.weatherbot.dto.WeatherForecastDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * @author Arthur Strokov
 * @email arthurstrokov@gmail.com
 * @created 27.09.2022
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PrintService {
    private final OpenApiProperties openApiProperties;
    private final OpenWeatherApiService openWeatherApiService;

    public String printCurrentWeather() {
        return openWeatherApiService.getCurrentWeatherByCity(
                openApiProperties.getCityName(),
                openApiProperties.getMode(),
                openApiProperties.getUnits(),
                openApiProperties.getLang(),
                openApiProperties.getOpenApiKey());
    }

    public String printWeatherForecast() {
        String weatherForecast =
                openWeatherApiService.getWeatherForecastByCity(
                        openApiProperties.getCityName(),
                        openApiProperties.getMode(),
                        openApiProperties.getUnits(),
                        openApiProperties.getLang(),
                        openApiProperties.getCnt(),
                        openApiProperties.getOpenApiKey());
        Gson gson = new GsonBuilder().create();
        WeatherForecastDto weatherForecastDto =
                gson.fromJson(weatherForecast, WeatherForecastDto.class);
        return PrintService.formatMessage(weatherForecastDto);
    }

    public String printWeatherForecast(String latitude, String longitude) {
        String weatherForecastByGeographicCoordinates =
                openWeatherApiService.getWeatherForecastByGeographicCoordinates(
                        String.valueOf(latitude),
                        String.valueOf(longitude),
                        openApiProperties.getMode(),
                        openApiProperties.getUnits(),
                        openApiProperties.getLang(),
                        openApiProperties.getCnt(),
                        openApiProperties.getOpenApiKey());
        Gson gson = new GsonBuilder().create();
        WeatherForecastDto weatherForecastDto =
                gson.fromJson(weatherForecastByGeographicCoordinates, WeatherForecastDto.class);
        return PrintService.formatMessage(weatherForecastDto);
    }

    public String printWeatherForecast(String city) {
        String weatherForecastByCity =
                openWeatherApiService.getWeatherForecastByCity(
                        city,
                        openApiProperties.getMode(),
                        openApiProperties.getUnits(),
                        openApiProperties.getLang(),
                        openApiProperties.getCnt(),
                        openApiProperties.getOpenApiKey());
        Gson gson = new GsonBuilder().create();
        WeatherForecastDto weatherForecastDto =
                gson.fromJson(weatherForecastByCity, WeatherForecastDto.class);
        return PrintService.formatMessage(weatherForecastDto);
    }

    private static String formatMessage(WeatherForecastDto weatherForecastDto) {
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
