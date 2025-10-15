package com.gmail.arthurstrokov.weather.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;
import wiremock.org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.file.Files;

import static java.nio.charset.StandardCharsets.UTF_8;

@UtilityClass
public class CommonUtils {

    @SneakyThrows
    public static String getJson(String pathname) {
        File file = new ClassPathResource(pathname).getFile();
        return FileUtils.readFileToString(file, UTF_8.name());
    }

    @SneakyThrows
    public static String readFileAsString(String path) {
        File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "__files" + path);
        return Files.readString(file.toPath());
    }
}
