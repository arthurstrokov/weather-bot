package com.gmail.arthurstrokov.weather.service;


import com.gmail.arthurstrokov.weather.gateway.ChatGateway;
import com.gmail.arthurstrokov.weather.gateway.WeatherGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    private final PromptService promptService;
    private final WeatherGateway weatherGateway;
    private final ChatGateway chatGateway;

    public String getWeatherForecastWithChat(String city) {
        log.info("Starting LLM request for city: {}", city);
        String weatherForecastByCity = weatherGateway.getWeatherForecastByCity(city);
        String prompt = promptService.generatePrompt(city, weatherForecastByCity);
        log.debug("Generated prompt for LLM: {}", prompt);
        String response = chatGateway.getChat(prompt);
        log.info("LLM request completed for city: {}", city);
        log.debug("LLM response: {}", response);
        return response;
    }
}
