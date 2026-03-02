package com.gmail.arthurstrokov.weather.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Coord {
    double lat;
    double lon;
}
