package com.gmail.arthurstrokov.weather.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Main {
    double temp;
    double feels_like;
    double temp_min;
    double temp_max;
    int pressure;
    int sea_level;
    int grnd_level;
    int humidity;
    double temp_kf;
}
