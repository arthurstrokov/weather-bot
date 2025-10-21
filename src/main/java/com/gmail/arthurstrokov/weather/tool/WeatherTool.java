package com.gmail.arthurstrokov.weather.tool;

import com.gmail.arthurstrokov.weather.service.ChatService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class WeatherTool {

    @Autowired
    private @Lazy ChatService chatService;

    @Tool(description = "Получение прогноза погоды")
    public String getWeatherForecastWithChat(@ToolParam(description = "Запрос пользователя") String city) {
        return chatService.getWeatherForecastWithChat(city);
    }
}
