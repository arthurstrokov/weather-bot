package com.gmail.arthurstrokov.weather.dto;

import com.google.gson.annotations.SerializedName;
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
public class Rain {
    @SerializedName("3h")
    Double threeHourVolume;
}
