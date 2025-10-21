package com.gmail.arthurstrokov.weather.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(name = "chat.implementation", havingValue = "local")
@Component
@RequiredArgsConstructor
public class LocalChatService implements ChatService {

    private final ChatModel chatModel;
    private final PromptService promptService;
    private final WeatherService weatherService;

    public String getWeatherForecastWithChat(String city) {
        String weatherForecastByCity = weatherService.getWeatherForecastByCity(city);
        String prompt = promptService.generatePrompt(city, weatherForecastByCity);
        return chatModel.call(prompt);
    }
}
