package com.gmail.arthurstrokov.weatherbot.service;

import com.gmail.arthurstrokov.weatherbot.configuration.OpenApiProperties;
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

import java.util.Objects;

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

    public void getCurrentWeather() {
        String getUrl = openApiProperties.getCurrentWeatherDataUrl() + openApiProperties.getOpenApiKey();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(getUrl, String.class);
        String weather = Objects.requireNonNull(responseEntity.getBody());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement je = JsonParser.parseString(weather);
        String prettyJsonString = gson.toJson(je);
        log.info(prettyJsonString);
    }

    public WeatherForecastDto getWeatherForecastData() {
        String getUrl = openApiProperties.getFiveDayWeatherForecastUrl() + openApiProperties.getOpenApiKey();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(getUrl, String.class);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        WeatherForecastDto weatherForecastDto = gson.fromJson(responseEntity.getBody(), WeatherForecastDto.class);
        log.info(weatherForecastDto.toString());
        return weatherForecastDto;
    }
}
