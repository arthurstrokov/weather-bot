package com.gmail.arthurstrokov.weather.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import static com.gmail.arthurstrokov.weather.template.PromptTemplate.PROMPT_TEMPLATE;

@Service
@RequiredArgsConstructor
public class PromptService {

    public String generatePrompt(String query, String context) {
        PromptTemplate promptTemplate = new PromptTemplate(PROMPT_TEMPLATE.getTemplate());
        promptTemplate.add("query", query);
        promptTemplate.add("context", context);
        return promptTemplate.render();
    }
}
