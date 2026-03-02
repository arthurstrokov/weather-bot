package com.gmail.arthurstrokov.weather.gateway;

import com.gmail.arthurstrokov.weather.client.OpenWeatherClient;
import com.gmail.arthurstrokov.weather.configuration.OpenWeatherProperties;
import com.gmail.arthurstrokov.weather.dto.WeatherForecastDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherGateway {

    private final OpenWeatherClient openWeatherClient;
    private final OpenWeatherProperties openWeatherProperties;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public String getCurrentWeatherByCity(String city) {
        String response = openWeatherClient.getCurrentWeatherByCity(
                city,
                openWeatherProperties.getMode(),
                openWeatherProperties.getUnits(),
                openWeatherProperties.getLang(),
                openWeatherProperties.getOpenApiKey()
        );
        log.debug("Current weather response for {}: {}", city, response);
        return response;
    }

    public String getCurrentWeatherByGeographicCoordinates(double latitude, double longitude) {
        String response = openWeatherClient.getCurrentWeatherByGeographicCoordinates(
                Double.toString(latitude),
                Double.toString(longitude),
                openWeatherProperties.getMode(),
                openWeatherProperties.getUnits(),
                openWeatherProperties.getLang(),
                openWeatherProperties.getOpenApiKey()
        );
        log.debug("Current weather response for coordinates ({}, {}): {}", latitude, longitude, response);
        return response;
    }

    public String getWeatherForecastByCity(String city) {
        WeatherForecastDTO forecast = openWeatherClient.getWeatherForecastByCity(
                city,
                openWeatherProperties.getMode(),
                openWeatherProperties.getUnits(),
                openWeatherProperties.getLang(),
                openWeatherProperties.getCnt(),
                openWeatherProperties.getOpenApiKey()
        );
        log.debug("Forecast response for {}: {} entries", city, forecast.getCnt());
        return gson.toJson(forecast);
    }

    public String getWeatherForecastByGeographicCoordinates(double latitude, double longitude) {
        WeatherForecastDTO forecast = openWeatherClient.getWeatherForecastByGeographicCoordinates(
                Double.toString(latitude),
                Double.toString(longitude),
                openWeatherProperties.getMode(),
                openWeatherProperties.getUnits(),
                openWeatherProperties.getLang(),
                openWeatherProperties.getCnt(),
                openWeatherProperties.getOpenApiKey()
        );
        log.debug("Forecast response for coordinates ({}, {}): {} entries", latitude, longitude, forecast.getCnt());
        return gson.toJson(forecast);
    }
}
