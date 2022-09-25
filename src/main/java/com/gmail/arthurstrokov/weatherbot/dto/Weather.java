package com.gmail.arthurstrokov.weatherbot.dto;

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
public class Weather {
    int id;
    String main;
    String description;
    String icon;
}
