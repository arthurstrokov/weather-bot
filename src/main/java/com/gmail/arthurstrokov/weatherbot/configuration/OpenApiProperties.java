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
    @Value("${open.api.url.current-day-url}")
    private String currentWeatherDataUrl;
    @Value("${open.api.url.five-day-url}")
    private String fiveDayWeatherForecastDataUrl;
}
