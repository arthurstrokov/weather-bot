package com.gmail.arthurstrokov.weather.controller;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.arthurstrokov.weather.util.CommonUtils.getJson;
import static com.gmail.arthurstrokov.weather.util.StubHelper.stubForGetREST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "open.api.url.base-url=http://localhost:${wiremock.server.port}",
        "open.api.key=test-api-key"
})
@AutoConfigureMockMvc
@WireMockTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class OpenWeatherApiControllerTest {

    private final MockMvc mockMvc;

    /**
     * Method under test: {@link OpenWeatherApiController#getWeatherForecast()}
     */
    @Test
    @SneakyThrows
    void getWeatherForecastTest() {
        //given
        stubForGetREST("/forecast?q=Minsk&mode=json&units=metric&lang=en&cnt=4&appid=test-api-key",
                "/forecast/expected.json");
        var expectedJsonResponse = "controller/expected.json";
        // when and then
        mockMvc.perform(get("/api/weather/forecast")
                        .contentType(APPLICATION_JSON_VALUE)
                )
                .andExpectAll(
                        status().isOk(),
                        content().contentType(APPLICATION_JSON_VALUE),
                        content().json(getJson(expectedJsonResponse)));
    }
}
