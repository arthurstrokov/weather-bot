package com.gmail.arthurstrokov.weather.configuration;

import feign.RequestInterceptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.wiremock.spring.EnableWireMock;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        properties = "spring.autoconfigure.exclude=org.telegram.telegrambots.longpolling.starter.TelegramBotStarterConfiguration"
)
@EnableWireMock
class OllamaFeignConfigTest {

    private final ApplicationContextRunner contextRunner =
            new ApplicationContextRunner().withUserConfiguration(OllamaFeignConfig.class);

    @Test
    void beanExistsWhenChatImplementationIsRemote() {
        contextRunner.withPropertyValues(
                        "chat.implementation=remote",
                        "ollama.api-key=test-key"   // убедитесь, что ключ свойства совпадает с @Value
                )
                .run(ctx -> assertThat(ctx).hasSingleBean(RequestInterceptor.class));
    }

    @Test
    void beanMissingWhenChatImplementationIsNotRemote() {
        contextRunner.withPropertyValues(
                        "chat.implementation=local",
                        "ollama.api-key=test-key"
                )
                .run(ctx -> assertThat(ctx).doesNotHaveBean(RequestInterceptor.class));
    }
}
