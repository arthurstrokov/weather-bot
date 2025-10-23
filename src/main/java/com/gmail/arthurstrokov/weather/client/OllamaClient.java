package com.gmail.arthurstrokov.weather.client;

import com.gmail.arthurstrokov.weather.configuration.OllamaFeignConfig;
import com.gmail.arthurstrokov.weather.model.ModelRequest;
import com.gmail.arthurstrokov.weather.model.ModelResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "ollama",
        url = "${ollama.base-url:https://ollama.com}",
        configuration = OllamaFeignConfig.class
)
public interface OllamaClient {

    @PostMapping(value = "/api/chat")
    ModelResponse chat(@RequestBody ModelRequest request);
}
