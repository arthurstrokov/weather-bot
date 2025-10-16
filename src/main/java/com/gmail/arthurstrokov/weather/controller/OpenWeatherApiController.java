package com.gmail.arthurstrokov.weather.controller;

import com.gmail.arthurstrokov.weather.service.OpenWeatherApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
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
@RequestMapping("api/weather")
@RequiredArgsConstructor
public class OpenWeatherApiController {

    private final OpenWeatherApiService openWeatherApiService;

    @GetMapping(value = "forecast", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getWeatherForecast() {
        return openWeatherApiService.getWeatherForecast();
    }

    @GetMapping(value = "current", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getCurrentWeather() {
        return openWeatherApiService.getCurrentWeather();
    }
}
