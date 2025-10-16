package com.gmail.arthurstrokov.weather.controller;

import com.gmail.arthurstrokov.weather.service.OpenWeatherApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    private final OpenWeatherApiService openWeatherApiService;

    @GetMapping(value = "/forecast", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getWeatherForecast() {
        try {
            String forecast = openWeatherApiService.getWeatherForecast();
            return ResponseEntity.ok(forecast);
        } catch (Exception e) {
            log.error("Error getting weather forecast", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error getting weather forecast: " + e.getMessage());
        }
    }

    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCurrentWeather() {
        try {
            String currentWeather = openWeatherApiService.getCurrentWeather();
            return ResponseEntity.ok(currentWeather);
        } catch (Exception e) {
            log.error("Error getting current weather", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error getting current weather: " + e.getMessage());
        }
    }
}
