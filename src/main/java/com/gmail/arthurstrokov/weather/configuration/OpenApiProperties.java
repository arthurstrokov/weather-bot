package com.gmail.arthurstrokov.weather.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author Arthur Strokov
 * @email arthurstrokov@gmail.com
 * @created 24.09.2022
 */
@Configuration
@Getter
@RefreshScope
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
}
