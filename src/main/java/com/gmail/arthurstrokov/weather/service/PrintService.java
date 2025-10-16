package com.gmail.arthurstrokov.weather.service;

import com.gmail.arthurstrokov.weather.dto.City;
import com.gmail.arthurstrokov.weather.dto.ForecastEntry;
import com.gmail.arthurstrokov.weather.dto.Rain;
import com.gmail.arthurstrokov.weather.dto.Weather;
import com.gmail.arthurstrokov.weather.dto.WeatherForecastDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Arthur Strokov
 * @email arthurstrokov@gmail.com
 * @created 27.09.2022
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PrintService {

    private static final String LINE_SEPARATOR = System.lineSeparator();

    private final OpenWeatherApiService openWeatherApiService;
    private final Gson gson = new GsonBuilder().create();

    public String printCurrentWeather() {
        return openWeatherApiService.getCurrentWeather();
    }

    public String printWeatherForecast() {
        var response = openWeatherApiService.getWeatherForecast();
        return formatResponse(response);
    }

    public String printWeatherForecast(double latitude, double longitude) {
        var response = openWeatherApiService.getWeatherForecastByGeographicCoordinates(latitude, longitude);
        return formatResponse(response);
    }

    public String printWeatherForecast(String city) {
        var response = openWeatherApiService.getWeatherForecastByCity(city);
        return formatResponse(response);
    }

    private String formatResponse(String rawResponse) {
        if (!StringUtils.hasText(rawResponse)) {
            return "Weather forecast is unavailable right now.";
        }
        try {
            WeatherForecastDto dto = gson.fromJson(rawResponse, WeatherForecastDto.class);
            return formatMessage(dto);
        } catch (Exception ex) {
            log.warn("Failed to parse weather forecast payload", ex);
            return "Weather forecast is unavailable right now.";
        }
    }

    private String formatMessage(WeatherForecastDto weatherForecastDto) {
        if (weatherForecastDto == null) {
            return "Weather forecast is unavailable right now.";
        }

        StringBuilder builder = new StringBuilder();
        String cityName = Optional.ofNullable(weatherForecastDto.getCity())
                .map(City::getName)
                .filter(StringUtils::hasText)
                .orElse("Unknown location");
        builder.append("City: ").append(cityName).append(LINE_SEPARATOR);

        List<ForecastEntry> entries = Optional.ofNullable(weatherForecastDto.getList()).orElse(List.of());

        if (CollectionUtils.isEmpty(entries)) {
            builder.append("No forecast data available.");
            return builder.toString();
        }

        entries.forEach(entry -> {
            builder.append("---").append(LINE_SEPARATOR);
            if (StringUtils.hasText(entry.getDateText())) {
                builder.append(entry.getDateText()).append(LINE_SEPARATOR);
            }
            if (entry.getMain() != null) {
                builder.append("Temp: ").append(entry.getMain().getTemp())
                        .append("C, feels like ")
                        .append(entry.getMain().getFeels_like())
                        .append("C").append(LINE_SEPARATOR);
            }
            appendWeatherDescription(builder, entry.getWeather());
            if (entry.getClouds() != null && entry.getClouds().getAll() != null) {
                builder.append("Clouds: ").append(entry.getClouds().getAll()).append('%').append(LINE_SEPARATOR);
            }
            appendRain(builder, entry.getRain());
            if (entry.getWind() != null) {
                builder.append("Wind: ")
                        .append(entry.getWind().getSpeed())
                        .append(" m/s, direction ")
                        .append(entry.getWind().getDeg())
                        .append(" deg").append(LINE_SEPARATOR);
            }
        });

        return builder.toString().trim();
    }

    private void appendWeatherDescription(StringBuilder builder, List<Weather> weather) {
        if (CollectionUtils.isEmpty(weather)) {
            return;
        }
        String description = weather.stream()
                .map(current -> {
                    String main = Optional.ofNullable(current.getMain()).orElse("Unknown");
                    String details = Optional.ofNullable(current.getDescription())
                            .filter(StringUtils::hasText)
                            .map(desc -> " (" + desc + ")")
                            .orElse("");
                    return main + details;
                })
                .collect(Collectors.joining(", "));
        builder.append(description).append(LINE_SEPARATOR);
    }

    private void appendRain(StringBuilder builder, Rain rain) {
        if (rain == null || rain.getThreeHourVolume() == null) {
            return;
        }
        builder.append("Rain (last 3h): ")
                .append(rain.getThreeHourVolume())
                .append(" mm")
                .append(LINE_SEPARATOR);
    }
}
