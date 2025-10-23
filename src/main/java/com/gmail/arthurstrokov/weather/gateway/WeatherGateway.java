package com.gmail.arthurstrokov.weather.gateway;

import com.gmail.arthurstrokov.weather.client.OpenWeatherClient;
import com.gmail.arthurstrokov.weather.configuration.OpenWeatherProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherGateway {

    private final OpenWeatherClient openWeatherClient;
    private final OpenWeatherProperties openWeatherProperties;

    public String getCurrentWeatherByCity(String city) {
        return openWeatherClient.getCurrentWeatherByCity(
                city,
                openWeatherProperties.getMode(),
                openWeatherProperties.getUnits(),
                openWeatherProperties.getLang(),
                openWeatherProperties.getOpenApiKey()
        );
    }

    public String getCurrentWeatherByGeographicCoordinates(double latitude, double longitude) {
        return openWeatherClient.getCurrentWeatherByGeographicCoordinates(
                Double.toString(latitude),
                Double.toString(longitude),
                openWeatherProperties.getMode(),
                openWeatherProperties.getUnits(),
                openWeatherProperties.getLang(),
                openWeatherProperties.getOpenApiKey()
        );
    }

    public String getWeatherForecastByCity(String city) {
        return openWeatherClient.getWeatherForecastByCity(
                city,
                openWeatherProperties.getMode(),
                openWeatherProperties.getUnits(),
                openWeatherProperties.getLang(),
                openWeatherProperties.getCnt(),
                openWeatherProperties.getOpenApiKey()
        );
    }

    public String getWeatherForecastByGeographicCoordinates(double latitude, double longitude) {
        return openWeatherClient.getWeatherForecastByGeographicCoordinates(
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
