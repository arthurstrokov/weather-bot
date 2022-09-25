package com.gmail.arthurstrokov.weatherbot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;

/**
 * @author Arthur Strokov
 * @email arthurstrokov@gmail.com
 * @created 25.09.2022
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WeatherForecastDto {
    String cod;
    int message;
    int cnt;
    ArrayList<Forecast> list;
    City city;
}

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
class City {
    int id;
    String name;
    Coord coord;
    String country;
    int population;
    int timezone;
    int sunrise;
    int sunset;
}

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
class Clouds {
    public int all;
}

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
class Coord {
    double lat;
    double lon;
}

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
class Forecast {
    int dt;
    Main main;
    ArrayList<Weather> weather;
    Clouds clouds;
    Wind wind;
    int visibility;
    double pop;
    Sys sys;
    String dt_txt;
    Rain rain;
}

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
class Main {
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

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
class Rain {
    @JsonProperty("3h")
    double _3h;
}

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
class Sys {
    String pod;
}

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
class Weather {
    int id;
    String main;
    String description;
    String icon;
}

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
class Wind {
    double speed;
    int deg;
    double gust;
}
