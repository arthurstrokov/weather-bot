package com.gmail.arthurstrokov.weather.service;

import com.gmail.arthurstrokov.weather.configuration.OpenApiProperties;
import com.gmail.arthurstrokov.weather.gateway.OpenWeatherApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Arthur Strokov
 * @email arthurstrokov@gmail.com
 * @created 24.09.2022
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OpenWeatherApiService {

    private final OpenWeatherApiClient openWeatherApiClient;
    private final OpenApiProperties openApiProperties;

    public String getCurrentWeather() {
        return getCurrentWeatherByCity(openApiProperties.getCityName());
    }

    public String getCurrentWeatherByCity(String cityName) {
        return openWeatherApiClient.getCurrentWeatherByCity(
                cityName,
                openApiProperties.getMode(),
                openApiProperties.getUnits(),
                openApiProperties.getLang(),
                openApiProperties.getOpenApiKey()
        );
    }

    public String getCurrentWeatherByGeographicCoordinates(double latitude, double longitude) {
        return openWeatherApiClient.getCurrentWeatherByGeographicCoordinates(
                Double.toString(latitude),
                Double.toString(longitude),
                openApiProperties.getMode(),
                openApiProperties.getUnits(),
                openApiProperties.getLang(),
                openApiProperties.getOpenApiKey()
        );
    }

    public String getWeatherForecast() {
        return getWeatherForecastByCity(openApiProperties.getCityName());
    }

    public String getWeatherForecastByCity(String cityName) {
        return openWeatherApiClient.getWeatherForecastByCity(
                cityName,
                openApiProperties.getMode(),
                openApiProperties.getUnits(),
                openApiProperties.getLang(),
                openApiProperties.getCnt(),
                openApiProperties.getOpenApiKey()
        );
    }

    public String getWeatherForecastByGeographicCoordinates(double latitude, double longitude) {
        return openWeatherApiClient.getWeatherForecastByGeographicCoordinates(
                Double.toString(latitude),
                Double.toString(longitude),
                openApiProperties.getMode(),
                openApiProperties.getUnits(),
                openApiProperties.getLang(),
                openApiProperties.getCnt(),
                openApiProperties.getOpenApiKey()
        );
    }
}
