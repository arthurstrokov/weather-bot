package com.gmail.arthurstrokov.weather.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author Arthur Strokov
 * @email arthurstrokov@gmail.com
 * @created 25.09.2022
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Coord {
    double lat;
    double lon;
}
