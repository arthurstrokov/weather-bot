package com.gmail.arthurstrokov.weather.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "open.weather")
public record OpenWeatherProperties(
        String key,
        Url url,
        Parameters parameters) {

    public OpenWeatherProperties {
        url = url == null ? new Url(null) : url;
        parameters = parameters == null
                ? new Parameters(null, null, null, null, null, null, null)
                : parameters;
    }

    /**
     * OpenWeather API access token supplied via environment.
     */
    public String getOpenApiKey() {
        return key;
    }

    public String getBaseUrl() {
        return url.baseUrl();
    }

    public String getCurrentPath() {
        return parameters.current();
    }

    public String getForecastPath() {
        return parameters.forecast();
    }

    public String getCityName() {
        return parameters.city();
    }

    public String getMode() {
        return parameters.mode();
    }

    public String getUnits() {
        return parameters.units();
    }

    public String getLang() {
        return parameters.lang();
    }

    public String getCnt() {
        return parameters.cnt();
    }

    public record Url(String baseUrl) {
    }

    public record Parameters(
            String current,
            String forecast,
            String city,
            String mode,
            String units,
            String lang,
            String cnt) {
    }
}
