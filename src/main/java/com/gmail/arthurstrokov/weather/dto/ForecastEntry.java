package com.gmail.arthurstrokov.weather.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public record ForecastEntry(long dt, Main main, List<Weather> weather, Clouds clouds, Wind wind, 
                            Integer visibility, Double pop, Sys sys, 
                            @SerializedName("dt_txt") String dateText, Rain rain) {
}
