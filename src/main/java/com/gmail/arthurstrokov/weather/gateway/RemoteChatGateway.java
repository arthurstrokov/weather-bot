package com.gmail.arthurstrokov.weather.gateway;

import com.gmail.arthurstrokov.weather.client.OllamaClient;
import com.gmail.arthurstrokov.weather.model.ChatMessage;
import com.gmail.arthurstrokov.weather.model.ModelRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.lang.Boolean.FALSE;

@ConditionalOnProperty(name = "chat.implementation", havingValue = "remote")
@Component
@RequiredArgsConstructor
public class RemoteChatGateway implements ChatGateway {

    private static final String USER_ROLE = "user";

    @Value("${ollama.model:gpt-oss:120b-cloud}")
    private String model;

    private final OllamaClient ollamaClient;

    @Override
    public String getChat(String prompt) {
        ModelRequest request = new ModelRequest(model, List.of(new ChatMessage(USER_ROLE, prompt)), FALSE);
        return ollamaClient.chat(request).message().content();
    }
}
