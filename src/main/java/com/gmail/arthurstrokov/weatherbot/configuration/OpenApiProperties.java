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
    @Value("${open.api.url.base-url}")
    private String baseUrl;
    @Value("${open.api.parameters.current}")
    private String current;
    @Value("${open.api.parameters.forecast}")
    private String forecast;
    @Value("${open.api.parameters.city}")
    private String cityName;
    @Value("${open.api.parameters.mode}")
    private String mode;
    @Value("${open.api.parameters.units}")
    private String units;
    @Value("${open.api.parameters.lang}")
    private String lang;
    @Value("${open.api.parameters.cnt}")
    private String cnt;

    public static final String CITY_NAME = "q";
    public static final String RESPONSE_FORMAT = "mode";
    public static final String UNITS_OF_MEASUREMENT = "units";
    public static final String NUMBER_OF_TIMESTAMPS = "cnt";
    public static final String APPID = "appid";
    public static final String LAT = "lat";
    public static final String LON = "lon";
}
