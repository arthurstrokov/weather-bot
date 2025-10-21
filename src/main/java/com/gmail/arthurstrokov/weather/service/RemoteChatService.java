package com.gmail.arthurstrokov.weather.service;

import com.gmail.arthurstrokov.weather.gateway.OllamaClient;
import com.gmail.arthurstrokov.weather.model.ModelRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@ConditionalOnProperty(name = "chat.implementation", havingValue = "remote")
@Component
@RequiredArgsConstructor
public class RemoteChatService implements ChatService {

    private final OllamaClient ollamaClient;
    private final PromptService promptService;
    private final WeatherService weatherService;

    public String getWeatherForecastWithChat(String city) {
        String weatherForecastByCity = weatherService.getWeatherForecastByCity(city);
        String prompt = promptService.generatePrompt(city, weatherForecastByCity);
        ModelRequest request = new ModelRequest("gpt-oss:20b", List.of(new ModelRequest.ChatMessage("user", prompt)), false);
        return ollamaClient.chat(request).message().content();
    }
}
