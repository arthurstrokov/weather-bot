package com.gmail.arthurstrokov.weather.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatModel chatModel;
    private final PromptService promptService;

    public String chat(String query, String context) {
        String prompt = promptService.generatePrompt(query, context);
        return chatModel.call(prompt);
    }
}
