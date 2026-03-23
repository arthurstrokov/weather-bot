package com.gmail.arthurstrokov.weather.model;

import java.time.Instant;

/**
 * DTO for Ollama /api/chat response.
 */
public record ModelResponse(String model,
                            Instant created_at,
                            Message message,
                            boolean done,
                            Long total_duration,
                            Long load_duration,
                            int prompt_eval_count,
                            Long prompt_eval_duration,
                            int eval_count,
                            Long eval_duration) {

    public record Message(String role, String content) {
    }
}

