package com.gmail.arthurstrokov.weather.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author Arthur Strokov
 * @email arthurstrokov@gmail.com
 * @created 24.09.2022
 */
@Getter
@Setter
@RefreshScope
@ConfigurationProperties(prefix = "open.api")
public class OpenApiProperties {

    /**
     * OpenWeather API access token supplied via environment.
     */
    private String key;

    private final Url url = new Url();
    private final Parameters parameters = new Parameters();

    public String getOpenApiKey() {
        return key;
    }

    public String getBaseUrl() {
        return url.getBaseUrl();
    }

    public String getCurrentPath() {
        return parameters.getCurrent();
    }

    public String getForecastPath() {
        return parameters.getForecast();
    }

    public String getCityName() {
        return parameters.getCity();
    }

    public String getMode() {
        return parameters.getMode();
    }

    public String getUnits() {
        return parameters.getUnits();
    }

    public String getLang() {
        return parameters.getLang();
    }

    public String getCnt() {
        return parameters.getCnt();
    }

    @Getter
    @Setter
    public static class Url {
        private String baseUrl;
    }

    @Getter
    @Setter
    public static class Parameters {
        private String current;
        private String forecast;
        private String city;
        private String mode;
        private String units;
        private String lang;
        private String cnt;
    }
}
