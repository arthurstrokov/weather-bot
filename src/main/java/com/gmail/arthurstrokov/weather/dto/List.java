package com.gmail.arthurstrokov.weather.dto;

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
public class List {
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
