package com.gmail.arthurstrokov.weather.dto;

import java.util.List;

public record WeatherForecastDTO(String cod, int message, int cnt, List<ForecastEntry> list, City city) {
}
