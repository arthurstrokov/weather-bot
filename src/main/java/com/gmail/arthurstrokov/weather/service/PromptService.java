package com.gmail.arthurstrokov.weather.service;

import com.gmail.arthurstrokov.weather.configuration.PromptProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PromptService {

    private final PromptProperties promptProperties;

    public String generatePrompt(String city, String context) {
        PromptTemplate promptTemplate = new PromptTemplate(promptProperties.getTemplate());
        promptTemplate.add("city", city);
        promptTemplate.add("context", context);
        return promptTemplate.render();
    }
}
