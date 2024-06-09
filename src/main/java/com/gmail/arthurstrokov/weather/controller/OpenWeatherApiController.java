package com.gmail.arthurstrokov.weather.controller;

import com.gmail.arthurstrokov.weather.configuration.OpenApiProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author Arthur Strokov
 * @email arthurstrokov@gmail.com
 * @created 24.09.2022
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class OpenWeatherApiController {

    private final RestTemplate restTemplate;
    private final OpenApiProperties openApiProperties;

    @RequestMapping
    public ResponseEntity<?> getCurrentWeatherDataBody() {
        String getResourceUrl = "https://api.openweathermap.org/data/2.5/forecast?q=Minsk&cnt=5&appid=" + openApiProperties.getOpenApiKey();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(getResourceUrl, String.class);
        return new ResponseEntity<>(responseEntity, HttpStatus.OK);
    }
}
