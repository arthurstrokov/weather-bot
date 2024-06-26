package com.gmail.arthurstrokov.weather.gateway;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Артур Александрович Строков
 * @email astrokov@clevertec.ru
 * @created 27.09.2022
 */
@FeignClient(name = "api-client", url = "${open.api.url.base-url}")
public interface OpenWeatherApiClient {

    @GetMapping("/weather")
    String getCurrentWeatherByCity(
            @RequestParam("q") String q,
            @RequestParam("mode") String mode,
            @RequestParam("units") String units,
            @RequestParam("lang") String lang,
            @RequestParam("appid") String appid
    );

    @GetMapping("/weather")
    String getCurrentWeatherByGeographicCoordinates(
            @RequestParam("lat") String lat,
            @RequestParam("lon") String lon,
            @RequestParam("mode") String mode,
            @RequestParam("units") String units,
            @RequestParam("lang") String appid,
            @RequestParam("appid") String lang
    );

    @GetMapping("/forecast")
    String getWeatherForecastByCity(
            @RequestParam("q") String q,
            @RequestParam("mode") String mode,
            @RequestParam("units") String units,
            @RequestParam("lang") String lang,
            @RequestParam("cnt") String cnt,
            @RequestParam("appid") String appid
    );

    @GetMapping("/forecast")
    String getWeatherForecastByGeographicCoordinates(
            @RequestParam("lat") String lat,
            @RequestParam("lon") String lon,
            @RequestParam("mode") String mode,
            @RequestParam("units") String units,
            @RequestParam("lang") String appid,
            @RequestParam("cnt") String cnt,
            @RequestParam("appid") String lang
    );
}
