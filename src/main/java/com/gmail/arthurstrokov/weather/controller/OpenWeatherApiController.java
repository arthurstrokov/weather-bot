package com.gmail.arthurstrokov.weather.controller;

import com.gmail.arthurstrokov.weather.configuration.OpenApiProperties;
import com.gmail.arthurstrokov.weather.gateway.OpenWeatherApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Arthur Strokov
 * @email arthurstrokov@gmail.com
 * @created 24.09.2022
 */
@Slf4j
@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class OpenWeatherApiController {

    private final OpenWeatherApiClient openWeatherApiClient;
    private final OpenApiProperties openApiProperties;

    @GetMapping("/forecast")
    public ResponseEntity<String> getWeatherForecast() {
        try {
            String forecast = openWeatherApiClient.getWeatherForecastByCity(
                    openApiProperties.getCityName(),
                    openApiProperties.getMode(),
                    openApiProperties.getUnits(),
                    openApiProperties.getLang(),
                    "5", // cnt
                    openApiProperties.getOpenApiKey()
            );
            return ResponseEntity.ok(forecast);
        } catch (Exception e) {
            log.error("Error getting weather forecast", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error getting weather forecast: " + e.getMessage());
        }
    }

    @GetMapping("/current")
    public ResponseEntity<String> getCurrentWeather() {
        try {
            String currentWeather = openWeatherApiClient.getCurrentWeatherByCity(
                    openApiProperties.getCityName(),
                    openApiProperties.getMode(),
                    openApiProperties.getUnits(),
                    openApiProperties.getLang(),
                    openApiProperties.getOpenApiKey()
            );
            return ResponseEntity.ok(currentWeather);
        } catch (Exception e) {
            log.error("Error getting current weather", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error getting current weather: " + e.getMessage());
        }
    }
}
