package com.gmail.arthurstrokov.weather.model;

import java.time.Instant;

/**
 * DTO for Ollama /api/chat response.
 */
public record ModelResponse(String model,
                            Instant created_at,
                            Message message,
                            boolean done,
                            long total_duration,
                            long load_duration,
                            int prompt_eval_count,
                            long prompt_eval_duration,
                            int eval_count,
                            long eval_duration) {

    public record Message(String role, String content) {
    }
}

