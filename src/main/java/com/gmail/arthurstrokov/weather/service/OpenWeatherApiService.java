package com.gmail.arthurstrokov.weather.service;

import com.gmail.arthurstrokov.weather.configuration.OpenWeatherProperties;
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
    private final OpenWeatherProperties openWeatherProperties;

    public String getCurrentWeather() {
        return getCurrentWeatherByCity(openWeatherProperties.getCityName());
    }

    public String getCurrentWeatherByCity(String cityName) {
        return openWeatherApiClient.getCurrentWeatherByCity(
                cityName,
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

    public String getWeatherForecast() {
        return getWeatherForecastByCity(openWeatherProperties.getCityName());
    }

    public String getWeatherForecastByCity(String cityName) {
        return openWeatherApiClient.getWeatherForecastByCity(
                cityName,
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
