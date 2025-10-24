package com.gmail.arthurstrokov.weather.tool;

import com.gmail.arthurstrokov.weather.service.WeatherService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class WeatherTool {

    private final WeatherService weatherService;

    public WeatherTool(@Lazy WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Tool(name = "getWeather", description = "Получение прогноза погоды")
    public String getWeather(@ToolParam(description = "Город") String city) {
        return weatherService.getWeatherForecastWithChat(city);
    }
}
