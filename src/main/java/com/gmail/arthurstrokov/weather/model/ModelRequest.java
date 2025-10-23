package com.gmail.arthurstrokov.weather.model;

import java.util.List;

/**
 * DTO for Ollama /api/chat request.
 */
public record ModelRequest(String model, List<ChatMessage> messages, Boolean stream) {
}
