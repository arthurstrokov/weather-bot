package com.gmail.arthurstrokov.weather.service;

import com.gmail.arthurstrokov.weather.configuration.OpenApiProperties;
import com.gmail.arthurstrokov.weather.gateway.OpenWeatherApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author Arthur Strokov
 * @email arthurstrokov@gmail.com
 * @created 24.09.2022
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OpenWeatherApiService {

    private final RestTemplate restTemplate;
    private final OpenApiProperties openApiProperties;
    private final OpenWeatherApiClient openWeatherApiClient;

    public String getCurrentWeatherByCity(String cityName, String mode, String units, String lang, String appid) {
        String currentWeatherByCity = openWeatherApiClient.getCurrentWeatherByCity(cityName, mode, units, lang, appid);
        return prettyPrintingJsonString(currentWeatherByCity);
    }

    public String getCurrentWeatherByGeographicCoordinates(String lat, String lon, String mode, String units, String lang, String appid) {
        String currentWeatherByGeographicCoordinates =
                openWeatherApiClient.getCurrentWeatherByGeographicCoordinates(lat, lon, mode, units, lang, appid);
        return prettyPrintingJsonString(currentWeatherByGeographicCoordinates);
    }

    public String getWeatherForecastByCity(String cityName, String mode, String units, String lang, String cnt, String appid) {
        String weatherForecastByCity = openWeatherApiClient.getWeatherForecastByCity(cityName, mode, units, lang, cnt, appid);
        return prettyPrintingJsonString(weatherForecastByCity);
    }

    public String getWeatherForecastByGeographicCoordinates(String lat, String lon, String mode, String units, String lang, String cnt,
                                                            String appid) {
        String weatherForecastByGeographicCoordinates =
                openWeatherApiClient.getWeatherForecastByGeographicCoordinates(lat, lon, mode, units, lang, cnt, appid);
        return prettyPrintingJsonString(weatherForecastByGeographicCoordinates);
    }

    private static String prettyPrintingJsonString(String s) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement je = JsonParser.parseString(s);
        return gson.toJson(je);
    }
}
