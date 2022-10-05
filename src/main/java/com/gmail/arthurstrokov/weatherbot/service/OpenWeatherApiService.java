package com.gmail.arthurstrokov.weatherbot.service;

import com.gmail.arthurstrokov.weatherbot.configuration.OpenApiProperties;
import com.gmail.arthurstrokov.weatherbot.gateway.OpenWeatherApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

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
        String currentWeatherByGeographicCoordinates = openWeatherApiClient.getCurrentWeatherByGeographicCoordinates(lat, lon, mode, units, lang, appid);
        return prettyPrintingJsonString(currentWeatherByGeographicCoordinates);
    }

    public String getWeatherForecastByCity(String cityName, String mode, String units, String lang, String cnt, String appid) {
        String weatherForecastByCity = openWeatherApiClient.getWeatherForecastByCity(cityName, mode, units, lang, cnt, appid);
        return prettyPrintingJsonString(weatherForecastByCity);
    }

    public String getWeatherForecastByGeographicCoordinates(String lat, String lon, String mode, String units, String lang, String cnt, String appid) {
        String weatherForecastByGeographicCoordinates = openWeatherApiClient.getWeatherForecastByGeographicCoordinates(lat, lon, mode, units, lang, cnt, appid);
        return prettyPrintingJsonString(weatherForecastByGeographicCoordinates);
    }

    @Deprecated
    public String getCurrentWeatherByCity() {
        String url = UriComponentsBuilder.fromHttpUrl(openApiProperties.getBaseUrl())
                .path(openApiProperties.getCurrent())
                .queryParam(OpenApiProperties.CITY_NAME, openApiProperties.getCityName())
                .queryParam(OpenApiProperties.RESPONSE_FORMAT, openApiProperties.getMode())
                .queryParam(OpenApiProperties.UNITS_OF_MEASUREMENT, openApiProperties.getUnits())
                .queryParam(OpenApiProperties.APPID, openApiProperties.getOpenApiKey())
                .encode().toUriString();
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        String weather = Objects.requireNonNull(responseEntity.getBody());
        return prettyPrintingJsonString(weather);
    }

    @Deprecated
    public String getCurrentWeatherByGeographicCoordinates(Double latitude, Double longitude) {
        String url = UriComponentsBuilder.fromHttpUrl(openApiProperties.getBaseUrl())
                .path(openApiProperties.getCurrent())
                .queryParam("lat", latitude)
                .queryParam("lon", longitude)
                .queryParam(OpenApiProperties.CITY_NAME, openApiProperties.getCityName())
                .queryParam(OpenApiProperties.RESPONSE_FORMAT, openApiProperties.getMode())
                .queryParam(OpenApiProperties.UNITS_OF_MEASUREMENT, openApiProperties.getUnits())
                .queryParam(OpenApiProperties.APPID, openApiProperties.getOpenApiKey())
                .encode().toUriString();
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        String weather = Objects.requireNonNull(responseEntity.getBody());
        return prettyPrintingJsonString(weather);
    }

    @Deprecated
    public String getWeatherForecastByCity() {
        String url = UriComponentsBuilder.fromHttpUrl(openApiProperties.getBaseUrl())
                .path(openApiProperties.getForecast())
                .queryParam(OpenApiProperties.CITY_NAME, openApiProperties.getCityName())
                .queryParam(OpenApiProperties.RESPONSE_FORMAT, openApiProperties.getMode())
                .queryParam(OpenApiProperties.UNITS_OF_MEASUREMENT, openApiProperties.getUnits())
                .queryParam(OpenApiProperties.NUMBER_OF_TIMESTAMPS, openApiProperties.getCnt())
                .queryParam(OpenApiProperties.APPID, openApiProperties.getOpenApiKey())
                .encode().toUriString();
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        String weather = Objects.requireNonNull(responseEntity.getBody());
        return prettyPrintingJsonString(weather);
    }

    @Deprecated
    public String getWeatherForecastByGeographicCoordinates(Double latitude, Double longitude) {
        String url = UriComponentsBuilder.fromHttpUrl(openApiProperties.getBaseUrl())
                .path(openApiProperties.getForecast())
                .queryParam(OpenApiProperties.LAT, latitude)
                .queryParam(OpenApiProperties.LON, longitude)
                .queryParam(OpenApiProperties.RESPONSE_FORMAT, openApiProperties.getMode())
                .queryParam(OpenApiProperties.UNITS_OF_MEASUREMENT, openApiProperties.getUnits())
                .queryParam(OpenApiProperties.NUMBER_OF_TIMESTAMPS, openApiProperties.getCnt())
                .queryParam(OpenApiProperties.APPID, openApiProperties.getOpenApiKey())
                .encode().toUriString();
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        String weather = Objects.requireNonNull(responseEntity.getBody());
        return prettyPrintingJsonString(weather);
    }

    private static String prettyPrintingJsonString(String s) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement je = JsonParser.parseString(s);
        return gson.toJson(je);
    }
}
