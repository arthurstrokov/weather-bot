package com.gmail.arthurstrokov.weatherbot.service;

import com.gmail.arthurstrokov.weatherbot.configuration.OpenApiProperties;
import com.gmail.arthurstrokov.weatherbot.dto.List;
import com.gmail.arthurstrokov.weatherbot.dto.Weather;
import com.gmail.arthurstrokov.weatherbot.dto.WeatherForecastDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Arthur Strokov
 * @email arthurstrokov@gmail.com
 * @created 24.09.2022
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OpenWeatherApiService {

    private final RestTemplate restTemplate;
    private final OpenApiProperties openApiProperties;

    public String getCurrentWeatherByCity() {
        String url = openApiProperties.getOpenApiBaseUrl() +
                openApiProperties.getCurrentWeatherDataUrlByCity() +
                openApiProperties.getOpenApiKey();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        String weather = Objects.requireNonNull(responseEntity.getBody());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement je = JsonParser.parseString(weather);
        String prettyJsonString = gson.toJson(je);
        log.info(prettyJsonString);
        return prettyJsonString;
    }

    public String getCurrentWeatherByGeographicCoordinates(Double latitude, Double longitude) {
        String url = openApiProperties.getOpenApiBaseUrl() +
                openApiProperties.getCurrentWeatherDataUrlByGeographicCoordinates()
                        .replace("{lat}", String.valueOf(latitude))
                        .replace("{lon}", String.valueOf(longitude)) +
                openApiProperties.getOpenApiKey();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        String weather = Objects.requireNonNull(responseEntity.getBody());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement je = JsonParser.parseString(weather);
        String prettyJsonString = gson.toJson(je);
        log.info(prettyJsonString);
        return prettyJsonString;
    }

    public String getWeatherForecastDataByCity() {
        String url = openApiProperties.getOpenApiBaseUrl() +
                openApiProperties.getFiveDayWeatherForecastUrlByCity() +
                openApiProperties.getOpenApiKey();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        Gson gson = new GsonBuilder().create();
        WeatherForecastDto weatherForecastDto = gson.fromJson(responseEntity.getBody(), WeatherForecastDto.class);
        return formatMessage(weatherForecastDto);
    }

    public String getWeatherForecastDataByGeographicCoordinates(Double latitude, Double longitude) {
        String url = openApiProperties.getOpenApiBaseUrl() +
                openApiProperties.getFiveDayWeatherForecastUrlByGeographicCoordinates()
                        .replace("{lat}", String.valueOf(latitude))
                        .replace("{lon}", String.valueOf(longitude)) +
                openApiProperties.getOpenApiKey();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        Gson gson = new GsonBuilder().create();
        WeatherForecastDto weatherForecastDto = gson.fromJson(responseEntity.getBody(), WeatherForecastDto.class);
        return formatMessage(weatherForecastDto);
    }

    private String formatMessage(WeatherForecastDto weatherForecastDto) {
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
        log.info(stringBuilder.toString());
        return stringBuilder.toString();
    }
}
