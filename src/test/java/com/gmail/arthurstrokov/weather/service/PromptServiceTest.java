package com.gmail.arthurstrokov.weather.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PromptServiceTest {

    private PromptService promptService;

    @BeforeEach
    void setUp() {
        promptService = new PromptService();
    }

    @Test
    @DisplayName("Should generate prompt with city and context")
    void generatePrompt() {
        // given
        String city = "Minsk";
        String context = "Temperature: 20°C, Rain: 50%";
        // when
        String result = promptService.generatePrompt(city, context);
        // then
        assertThat(result)
                .isNotBlank()
                .contains(city)
                .contains(context);
    }

    @Test
    @DisplayName("Should generate prompt for different cities")
    void generatePromptForDifferentCities() {
        // given
        String city = "London";
        String context = "Cloudy, 15°C";
        // when
        String result = promptService.generatePrompt(city, context);
        // then
        assertThat(result)
                .contains("London")
                .contains("Cloudy, 15°C");
    }

    @Test
    @DisplayName("Should generate prompt with template structure")
    void generatePromptContainsTemplateStructure() {
        // given
        String city = "Paris";
        String context = "Sunny, 25°C";
        // when
        String result = promptService.generatePrompt(city, context);
        // then
        assertThat(result)
                .contains("Paris")
                .contains("Sunny, 25°C");
    }
}
