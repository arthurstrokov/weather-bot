package com.gmail.arthurstrokov.weather.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ForecastEntry {
    long dt;
    Main main;
    List<Weather> weather = new ArrayList<>();
    Clouds clouds;
    Wind wind;
    Integer visibility;
    Double pop;
    Sys sys;
    @SerializedName("dt_txt")
    String dateText;
    Rain rain;
}
