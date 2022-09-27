package com.gmail.arthurstrokov.weatherbot.gateway;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Артур Александрович Строков
 * @email astrokov@clevertec.ru
 * @created 27.09.2022
 */
@FeignClient(name = "api-client", url = "${open.api.url.current-day-url}")
public interface OpenWeatherApiClient {
    @GetMapping
    String getCurrentWeatherByCity(@RequestParam("q") String q, @RequestParam("appid") String appid);
}
