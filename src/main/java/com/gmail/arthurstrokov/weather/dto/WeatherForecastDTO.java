package com.gmail.arthurstrokov.weather.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WeatherForecastDTO {
    String cod;
    int message;
    int cnt;
    List<ForecastEntry> list;
    City city;
}
