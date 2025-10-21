package com.gmail.arthurstrokov.weather.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Minimal DTO for Ollama /api/chat response.
 * We only need the assistant message content; ignore other fields to keep it robust.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record OllamaChatResponse(ChatMessage message) {
}
