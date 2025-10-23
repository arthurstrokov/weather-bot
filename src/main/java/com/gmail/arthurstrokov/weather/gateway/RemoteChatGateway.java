package com.gmail.arthurstrokov.weather.gateway;

import com.gmail.arthurstrokov.weather.client.OllamaClient;
import com.gmail.arthurstrokov.weather.model.ChatMessage;
import com.gmail.arthurstrokov.weather.model.ModelRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@ConditionalOnProperty(name = "chat.implementation", havingValue = "remote")
@Component
@RequiredArgsConstructor
public class RemoteChatGateway implements ChatGateway {

    private final OllamaClient ollamaClient;

    @Override
    public String getChat(String prompt) {
        ModelRequest request = new ModelRequest("gpt-oss:20b", List.of(new ChatMessage("user", prompt)), false);
        return ollamaClient.chat(request).message().content();
    }
}
