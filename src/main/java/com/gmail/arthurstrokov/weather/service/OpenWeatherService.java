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

    public String getCurrentWeatherByCity(String city) {
        return openWeatherApiClient.getCurrentWeatherByCity(
                city,
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

    public String getWeatherForecastByCity(String city) {
        return openWeatherApiClient.getWeatherForecastByCity(
                city,
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
