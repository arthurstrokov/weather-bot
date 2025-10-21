package com.gmail.arthurstrokov.weather.controller;

import com.gmail.arthurstrokov.weather.service.OpenWeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/weather")
@RequiredArgsConstructor
public class OpenWeatherApiController {

    private final OpenWeatherService openWeatherService;

    @GetMapping(value = "forecast", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getWeatherForecast(@RequestParam String city) {
        return openWeatherService.getWeatherForecastByCity(city);
    }

    @GetMapping(value = "current", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getCurrentWeather(@RequestParam String city) {
        return openWeatherService.getCurrentWeatherByCity(city);
    }
}
