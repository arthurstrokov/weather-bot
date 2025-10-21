package com.gmail.arthurstrokov.weather.service;

import com.gmail.arthurstrokov.weather.gateway.OllamaClient;
import com.gmail.arthurstrokov.weather.model.ChatMessage;
import com.gmail.arthurstrokov.weather.model.ChatRequest;
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
    private final OpenWeatherService openWeatherService;

    public String getWeatherForecastWithChat(String city) {
        String weatherForecastByCity = openWeatherService.getWeatherForecastByCity(city);
        String prompt = promptService.generatePrompt(city, weatherForecastByCity);
        ChatRequest request = new ChatRequest("gpt-oss:20b", List.of(new ChatMessage("user", prompt)), false);
        return ollamaClient.chat(request).message().content();
    }
}
