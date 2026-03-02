package com.gmail.arthurstrokov.weather.gateway;

import com.gmail.arthurstrokov.weather.client.OllamaClient;
import com.gmail.arthurstrokov.weather.model.ModelRequest;
import com.gmail.arthurstrokov.weather.model.ModelResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;

import static java.lang.Boolean.FALSE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RemoteChatGatewayTest {

    @Mock
    private OllamaClient ollamaClient;

    @Captor
    private ArgumentCaptor<ModelRequest> requestCaptor;

    private RemoteChatGateway remoteChatGateway;

    @BeforeEach
    void setUp() {
        remoteChatGateway = new RemoteChatGateway(ollamaClient);
        ReflectionTestUtils.setField(remoteChatGateway, "model", "gpt-oss:120b-cloud");
    }

    @Test
    @DisplayName("Should get chat response from remote Ollama API")
    void getChat() {
        // given
        String prompt = "What is the weather in Minsk?";
        String expectedResponse = "The weather in Minsk is sunny with 20°C.";
        ModelResponse.Message message = new ModelResponse.Message("assistant", expectedResponse);
        ModelResponse modelResponse = new ModelResponse(
                "gpt-oss:120b-cloud",
                Instant.now(),
                message,
                true,
                0, 0, 0, 0, 0, 0
        );
        when(ollamaClient.chat(any(ModelRequest.class))).thenReturn(modelResponse);
        // when
        String result = remoteChatGateway.getChat(prompt);
        // then
        assertThat(result).isEqualTo(expectedResponse);
        verify(ollamaClient).chat(requestCaptor.capture());
        ModelRequest capturedRequest = requestCaptor.getValue();
        assertThat(capturedRequest.model()).isEqualTo("gpt-oss:120b-cloud");
        assertThat(capturedRequest.messages()).hasSize(1);
        assertThat(capturedRequest.stream()).isEqualTo(FALSE);
    }

    @Test
    @DisplayName("Should send user role in chat message")
    void getChatSendsUserRole() {
        // given
        String prompt = "Weather forecast";
        ModelResponse.Message message = new ModelResponse.Message("assistant", "Sunny");
        ModelResponse modelResponse = new ModelResponse(
                "gpt-oss:120b-cloud",
                Instant.now(),
                message,
                true,
                0, 0, 0, 0, 0, 0
        );
        when(ollamaClient.chat(any(ModelRequest.class))).thenReturn(modelResponse);
        // when
        remoteChatGateway.getChat(prompt);
        // then
        verify(ollamaClient).chat(requestCaptor.capture());
        ModelRequest capturedRequest = requestCaptor.getValue();
        com.gmail.arthurstrokov.weather.model.ChatMessage msg = capturedRequest.messages().getFirst();
        assertThat(msg)
                .extracting(com.gmail.arthurstrokov.weather.model.ChatMessage::role,
                        com.gmail.arthurstrokov.weather.model.ChatMessage::content)
                .containsExactly("user", prompt);
    }
}
