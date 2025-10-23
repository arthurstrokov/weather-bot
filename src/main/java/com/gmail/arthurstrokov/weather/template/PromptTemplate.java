package com.gmail.arthurstrokov.weather.template;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PromptTemplate {

    PROMPT_TEMPLATE("""
            
            Write a short weather forecast for the city {city} with data {context}.
            
            Include temperature, precipitation, and wind conditions.
            Use a cheerful and friendly tone, as if it's for a social media post.
            
            Follow these rules:
            
            1. Avoid statements like "Based on the context..." or "The provided information...".
            2. Answer in Russian.
            
            """);

    private final String template;
}
