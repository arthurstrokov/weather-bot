package com.gmail.arthurstrokov.weather.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.wiremock.spring.EnableWireMock;

import static com.github.tomakehurst.wiremock.client.WireMock.exactly;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.gmail.arthurstrokov.weather.util.StubHelper.stubForGetREST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.autoconfigure.exclude=org.telegram.telegrambots.longpolling.starter.TelegramBotStarterConfiguration"
)
@EnableWireMock
class WeatherControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * Method under test: {@link WeatherController#getWeatherForecast(String)}
     */
    @Test
    @DisplayName("Should return weather forecast for a given city")
    @SneakyThrows
    void getWeatherForecastTest() {
        // given
        stubForGetREST("/forecast?q=Minsk&mode=json&units=metric&lang=en&cnt=4&appid=key",
                "/forecast/expected.json");
        // when & then
        mockMvc.perform(get("/api/weather/forecast")
                        .contentType(APPLICATION_JSON_VALUE)
                        .param("city", "Minsk"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(APPLICATION_JSON_VALUE)
                );
        verify(exactly(1), getRequestedFor(urlEqualTo("/forecast?q=Minsk&mode=json&units=metric&lang=en&cnt=4&appid=key")));
    }
}
