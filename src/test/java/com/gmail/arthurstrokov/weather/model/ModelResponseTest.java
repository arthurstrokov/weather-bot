package com.gmail.arthurstrokov.weather.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class ModelResponseTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("Should deserialize ModelResponse with null Long fields")
    void shouldDeserializeWithNullLongFields() throws Exception {
        // given
        String json = """
            {
                "model": "gpt-oss:20b",
                "created_at": "2024-01-01T00:00:00Z",
                "message": {
                    "role": "assistant",
                    "content": "Test response"
                },
                "done": true,
                "total_duration": null,
                "load_duration": null,
                "prompt_eval_count": 10,
                "prompt_eval_duration": null,
                "eval_count": 5,
                "eval_duration": null
            }
        """;

        // when
        ModelResponse result = objectMapper.readValue(json, ModelResponse.class);

        // then
        assertThat(result).isNotNull();
        assertThat(result.model()).isEqualTo("gpt-oss:20b");
        assertThat(result.created_at()).isEqualTo(Instant.parse("2024-01-01T00:00:00Z"));
        assertThat(result.message().role()).isEqualTo("assistant");
        assertThat(result.message().content()).isEqualTo("Test response");
        assertThat(result.done()).isTrue();
        assertThat(result.total_duration()).isNull();
        assertThat(result.load_duration()).isNull();
        assertThat(result.prompt_eval_count()).isEqualTo(10);
        assertThat(result.prompt_eval_duration()).isNull();
        assertThat(result.eval_count()).isEqualTo(5);
        assertThat(result.eval_duration()).isNull();
    }

    @Test
    @DisplayName("Should deserialize ModelResponse with non-null Long fields")
    void shouldDeserializeWithNonNullLongFields() throws Exception {
        // given
        String json = """
            {
                "model": "gpt-oss:20b",
                "created_at": "2024-01-01T00:00:00Z",
                "message": {
                    "role": "assistant",
                    "content": "Test response"
                },
                "done": true,
                "total_duration": 1000,
                "load_duration": 500,
                "prompt_eval_count": 10,
                "prompt_eval_duration": 300,
                "eval_count": 5,
                "eval_duration": 200
            }
        """;

        // when
        ModelResponse result = objectMapper.readValue(json, ModelResponse.class);

        // then
        assertThat(result).isNotNull();
        assertThat(result.total_duration()).isEqualTo(1000L);
        assertThat(result.load_duration()).isEqualTo(500L);
        assertThat(result.prompt_eval_duration()).isEqualTo(300L);
        assertThat(result.eval_duration()).isEqualTo(200L);
    }

    @Test
    @DisplayName("Should deserialize ModelResponse with mixed null and non-null Long fields")
    void shouldDeserializeWithMixedNullAndNonNullFields() throws Exception {
        // given
        String json = """
            {
                "model": "gpt-oss:20b",
                "created_at": "2024-01-01T00:00:00Z",
                "message": {
                    "role": "assistant",
                    "content": "Test response"
                },
                "done": true,
                "total_duration": 1000,
                "load_duration": null,
                "prompt_eval_count": 10,
                "prompt_eval_duration": 300,
                "eval_count": 5,
                "eval_duration": null
            }
        """;

        // when
        ModelResponse result = objectMapper.readValue(json, ModelResponse.class);

        // then
        assertThat(result).isNotNull();
        assertThat(result.total_duration()).isEqualTo(1000L);
        assertThat(result.load_duration()).isNull();
        assertThat(result.prompt_eval_duration()).isEqualTo(300L);
        assertThat(result.eval_duration()).isNull();
    }
}