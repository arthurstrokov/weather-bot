package com.gmail.arthurstrokov.weather.dto;

import com.google.gson.annotations.SerializedName;

public record Rain(@SerializedName("3h") Double threeHourVolume) {
}
