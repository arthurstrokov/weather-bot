package com.gmail.arthurstrokov.weather.gateway;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.model.ChatModel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@EnabledIfEnvironmentVariable(named = "test.implementation", matches = "local")
@ExtendWith(MockitoExtension.class)
class LocalChatGatewayTest {

    @Mock
    private ChatModel chatModel;

    private LocalChatGateway localChatGateway;

    @BeforeEach
    void setUp() {
        localChatGateway = new LocalChatGateway(chatModel);
    }

    @Test
    @DisplayName("Should get chat response from local model")
    void getChat() {
        // given
        String prompt = "What is the weather in Minsk?";
        String expectedResponse = "The weather in Minsk is sunny with 20°C.";
        when(chatModel.call(anyString())).thenReturn(expectedResponse);
        // when
        String result = localChatGateway.getChat(prompt);
        // then
        assertThat(result).isEqualTo(expectedResponse);
        verify(chatModel).call(prompt);
    }

    @Test
    @DisplayName("Should handle empty prompt")
    void getChatWithEmptyPrompt() {
        // given
        String prompt = "";
        when(chatModel.call(anyString())).thenReturn("");
        // when
        String result = localChatGateway.getChat(prompt);
        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should handle complex prompt")
    void getChatWithComplexPrompt() {
        // given
        String prompt = """
                Based on the following weather data for Minsk:
                Temperature: 20°C
                Conditions: Sunny
                Humidity: 65%
                Provide a short forecast.
                """;
        String expectedResponse = "Minsk will have sunny weather with 20°C and 65% humidity.";
        when(chatModel.call(anyString())).thenReturn(expectedResponse);
        // when
        String result = localChatGateway.getChat(prompt);
        // then
        assertThat(result).isEqualTo(expectedResponse);
    }
}
