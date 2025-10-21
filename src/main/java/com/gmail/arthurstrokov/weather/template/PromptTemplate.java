package com.gmail.arthurstrokov.weather.template;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PromptTemplate {

    PROMPT_TEMPLATE("""
            
            {query}
            
            {context}
            
            Follow these rules:
            
            1. If the answer is not in the context, just say that you don't know.
            2. Avoid statements like "Based on the context..." or "The provided information...".
            3. Answer in Russian.
            """);

    private final String template;
}
