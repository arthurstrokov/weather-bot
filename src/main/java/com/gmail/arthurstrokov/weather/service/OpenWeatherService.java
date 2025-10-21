package com.gmail.arthurstrokov.weather.service;

import com.gmail.arthurstrokov.weather.configuration.OpenWeatherProperties;
import com.gmail.arthurstrokov.weather.gateway.OpenWeatherApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OpenWeatherService {

    private final OpenWeatherApiClient openWeatherApiClient;
    private final OpenWeatherProperties openWeatherProperties;
    private final ChatService chatService;

    public String getCurrentWeatherByCity() {
        return openWeatherApiClient.getCurrentWeatherByCity(
                openWeatherProperties.getCityName(),
                openWeatherProperties.getMode(),
                openWeatherProperties.getUnits(),
                openWeatherProperties.getLang(),
                openWeatherProperties.getOpenApiKey()
        );
    }

    public String getCurrentWeatherByGeographicCoordinates(double latitude, double longitude) {
        return openWeatherApiClient.getCurrentWeatherByGeographicCoordinates(
                Double.toString(latitude),
                Double.toString(longitude),
                openWeatherProperties.getMode(),
                openWeatherProperties.getUnits(),
                openWeatherProperties.getLang(),
                openWeatherProperties.getOpenApiKey()
        );
    }

    public String getWeatherForecastWithChat(String query) {
        return chatService.chat(query, getWeatherForecastByCity());
    }

    public String getWeatherForecastByCity() {
        return openWeatherApiClient.getWeatherForecastByCity(
                openWeatherProperties.getCityName(),
                openWeatherProperties.getMode(),
                openWeatherProperties.getUnits(),
                openWeatherProperties.getLang(),
                openWeatherProperties.getCnt(),
                openWeatherProperties.getOpenApiKey()
        );
    }

    public String getWeatherForecastByGeographicCoordinates(double latitude, double longitude) {
        return openWeatherApiClient.getWeatherForecastByGeographicCoordinates(
                Double.toString(latitude),
                Double.toString(longitude),
                openWeatherProperties.getMode(),
                openWeatherProperties.getUnits(),
                openWeatherProperties.getLang(),
                openWeatherProperties.getCnt(),
                openWeatherProperties.getOpenApiKey()
        );
    }
}
