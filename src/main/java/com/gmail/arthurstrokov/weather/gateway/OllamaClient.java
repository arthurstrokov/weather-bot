package com.gmail.arthurstrokov.weather.gateway;

import com.gmail.arthurstrokov.weather.configuration.OllamaFeignConfig;
import com.gmail.arthurstrokov.weather.model.ChatRequest;
import com.gmail.arthurstrokov.weather.model.OllamaChatResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "ollama",
        url = "${ollama.base-url:https://ollama.com}",
        configuration = OllamaFeignConfig.class
)
public interface OllamaClient {

    @PostMapping(value = "/api/chat", consumes = "application/json")
    OllamaChatResponse chat(@RequestBody ChatRequest request);
}
