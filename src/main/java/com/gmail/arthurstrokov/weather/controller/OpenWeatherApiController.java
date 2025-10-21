package com.gmail.arthurstrokov.weather.controller;

import com.gmail.arthurstrokov.weather.service.OpenWeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/weather")
@RequiredArgsConstructor
public class OpenWeatherApiController {

    private final OpenWeatherService openWeatherService;

    @GetMapping(value = "forecast", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getWeatherForecast() {
        return openWeatherService.getWeatherForecastByCity();
    }

    @GetMapping(value = "current", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getCurrentWeather() {
        return openWeatherService.getCurrentWeatherByCity();
    }
}
