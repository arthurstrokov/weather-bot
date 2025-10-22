package com.gmail.arthurstrokov.weather.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.FactCheckingEvaluator;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.wiremock.spring.EnableWireMock;

import java.util.List;

import static com.gmail.arthurstrokov.weather.util.StubHelper.stubForGetREST;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnabledIfEnvironmentVariable(named = "test.implementation", matches = "local")
@Slf4j
@SpringBootTest(
        properties = "spring.autoconfigure.exclude=org.telegram.telegrambots.longpolling.starter.TelegramBotStarterConfiguration"
)
@EnableWireMock
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class LocalChatServiceTest {

    private final LocalChatService localChatService;
    private final ChatModel chatModel;

    /**
     * Method under test: {@link LocalChatService#getWeatherForecastWithChat(String)}
     * <p>
     * Verifies that LocalChatService interacts with the chat model to produce a weather forecast.
     * The output is evaluated using a fact-checking evaluator to ensure it meets expected criteria.
     * The model must support fact-checking. If possible, it should also return score and metadata
     * to enrich the evaluation result.
     */
    @Test
    @DisplayName("Should interact with chat")
    void getWeatherForecastWithChat() {
        // given
        stubForGetREST("/forecast?q=Minsk&mode=json&units=metric&lang=en&cnt=4&appid=key",
                "/forecast/expected.json");
        String minsk = localChatService.getWeatherForecastWithChat("Minsk");
        log.info(minsk);

        String userText = """
                Оцени, является ли следующий текст прогнозом погоды.
                Признаки прогноза: указание города, времени суток, температуры, погодных условий (дождь, облачность, ветер).
                """;

        String criteria = """
                Текст должен быть признан прогнозом погоды, если он содержит:
                - Упоминание города или региона
                - Временные интервалы (например, "утро", "вечер", "завтра")
                - Температурные значения (в градусах Цельсия или Фаренгейта)
                - Описание погодных условий (дождь, облачность, ветер, солнце и т.д.)
                
                Если все эти признаки присутствуют, тест считается пройденным.
                """;

        EvaluationRequest request = new EvaluationRequest(
                userText,
                List.of(new Document(criteria)),
                minsk);

        FactCheckingEvaluator evaluator = new FactCheckingEvaluator(ChatClient.builder(chatModel));
        EvaluationResponse response = evaluator.evaluate(request);

        assertTrue(response.isPass(), () -> "LLM evaluation failed: " + response.isPass());
    }
}
