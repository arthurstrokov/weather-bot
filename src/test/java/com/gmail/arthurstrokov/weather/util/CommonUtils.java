package com.gmail.arthurstrokov.weather.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.InputStream;

import static java.nio.charset.StandardCharsets.UTF_8;

@UtilityClass
public class CommonUtils {

    @SneakyThrows
    public static String getJson(String pathname) {
        try (InputStream inputStream = new ClassPathResource(pathname).getInputStream()) {
            return StreamUtils.copyToString(inputStream, UTF_8);
        }
    }

    @SneakyThrows
    public static String readFileAsString(String path) {
        try (InputStream inputStream = new ClassPathResource("__files" + path).getInputStream()) {
            return StreamUtils.copyToString(inputStream, UTF_8);
        }
    }
}
