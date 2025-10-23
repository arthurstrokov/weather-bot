package com.gmail.arthurstrokov.weather.gateway;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(name = "chat.implementation", havingValue = "local")
@Component
@RequiredArgsConstructor
public class LocalChatGateway implements ChatGateway {

    private final ChatModel chatModel;

    @Override
    public String getChat(String prompt) {
        return chatModel.call(prompt);
    }
}
