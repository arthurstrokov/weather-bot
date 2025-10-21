package com.gmail.arthurstrokov.weather.tool;

import com.gmail.arthurstrokov.weather.service.OpenWeatherService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class WeatherTool {

    @Autowired
    private @Lazy OpenWeatherService openWeatherService;

    @Tool(description = "Получение прогноза погоды")
    public String getWeatherForecast(@ToolParam(description = "Запрос пользователя") String query) {
        return openWeatherService.getWeatherForecastWithChat(query);
    }
}
