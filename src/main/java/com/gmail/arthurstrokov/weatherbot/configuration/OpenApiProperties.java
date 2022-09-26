package com.gmail.arthurstrokov.weatherbot.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Arthur Strokov
 * @email arthurstrokov@gmail.com
 * @created 24.09.2022
 */
@Configuration
@Getter
public class OpenApiProperties {

    @Value("${open.api.key}")
    private String openApiKey;
    @Value("${open.api.base-url}")
    private String openApiBaseUrl;
    @Value("${open.api.url.current}")
    private String currentWeatherDataUrlByCity;
    @Value("${open.api.url.current-gc}")
    private String currentWeatherDataUrlByGeographicCoordinates;
    @Value("${open.api.url.five-day-city}")
    private String fiveDayWeatherForecastUrlByCity;
    @Value("${open.api.url.five-day-gc}")
    private String fiveDayWeatherForecastUrlByGeographicCoordinates;
}
