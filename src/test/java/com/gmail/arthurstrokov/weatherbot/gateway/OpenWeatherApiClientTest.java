package com.gmail.arthurstrokov.weatherbot.gateway;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.gmail.arthurstrokov.weatherbot.service.OpenWeatherApiService;
import com.gmail.arthurstrokov.weatherbot.util.ServiceHelper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.FileNotFoundException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

/**
 * @author Артур Александрович Строков
 * @email astrokov@clevertec.ru
 * @created 04.10.2022
 */
@SpringBootTest
@ActiveProfiles("test")
class OpenWeatherApiClientTest {
    private static final WireMockServer wireMockServer = new WireMockServer(options().port(8888));
    @Autowired
    private OpenWeatherApiService openWeatherApiService;

    private static String expected;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("https://api.openweathermap.org/data/2.5", wireMockServer::baseUrl);
    }

    @BeforeAll
    public static void beforeAll() throws FileNotFoundException {
        expected = ServiceHelper.getJsonFromFile("expected.json");
    }

    @BeforeEach
    public void setup() {
        wireMockServer.start();
    }

    @AfterEach
    public void afterEach() {
        wireMockServer.stop();
        wireMockServer.resetAll();
    }

    @Test
    void context() {
        Assertions.assertEquals("http://localhost:8888", wireMockServer.baseUrl());
        Assertions.assertTrue(wireMockServer.isRunning());
    }

    @Test
    @Description("get current weather by city")
    void testGetCurrentWeatherByCity() {
        wireMockServer.stubFor(get(urlPathMatching("/weather"))
                .withQueryParam("q", WireMock.equalTo("Minsk"))
                .withQueryParam("mode", WireMock.equalTo("json"))
                .withQueryParam("units", WireMock.equalTo("metric"))
                .withQueryParam("lang", WireMock.equalTo("en"))
                .withQueryParam("appid", WireMock.equalTo(""))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.OK.value())
                        .withBody("[{}]")));
        String result = openWeatherApiService.getCurrentWeatherByCity("Minsk", "json", "metric", "en", "");

        Assertions.assertEquals("[\n  {}\n]", result);
    }

    @Test
    @Description("get current weather by geographic coordinates")
    void testGetCurrentWeatherByGeographicCoordinates() {
        wireMockServer.stubFor(get(urlPathMatching("/weather"))
                .withQueryParam("lat", WireMock.equalTo("0"))
                .withQueryParam("lon", WireMock.equalTo("0"))
                .withQueryParam("mode", WireMock.equalTo("json"))
                .withQueryParam("units", WireMock.equalTo("metric"))
                .withQueryParam("lang", WireMock.equalTo("en"))
                .withQueryParam("appid", WireMock.equalTo(""))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.OK.value())
                        .withBody("[{}]")));
        String result = openWeatherApiService.getCurrentWeatherByGeographicCoordinates("0", "0", "json", "metric", "en", "");

        Assertions.assertEquals("[\n  {}\n]", result);
    }

    @Test
    @Description("get weather forecast by city")
    void testGetWeatherForecastByCity() {
        wireMockServer.stubFor(get(urlPathMatching("/forecast"))
                .withQueryParam("q", WireMock.equalTo("Minsk"))
                .withQueryParam("mode", WireMock.equalTo("json"))
                .withQueryParam("units", WireMock.equalTo("metric"))
                .withQueryParam("lang", WireMock.equalTo("en"))
                .withQueryParam("cnt", WireMock.equalTo("4"))
                .withQueryParam("appid", WireMock.equalTo(""))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.OK.value())
                        .withBody(expected)));
        String result = openWeatherApiService.getWeatherForecastByCity("Minsk", "json", "metric", "en", "4", "");

        Assertions.assertEquals(expected, result);
    }

    @Test
    @Description("get weather forecast by geographic coordinates")
    void testGetWeatherForecastByGeographicCoordinates() {
        wireMockServer.stubFor(get(urlPathMatching("/forecast"))
                .withQueryParam("lat", WireMock.equalTo("0"))
                .withQueryParam("lon", WireMock.equalTo("0"))
                .withQueryParam("mode", WireMock.equalTo("json"))
                .withQueryParam("units", WireMock.equalTo("metric"))
                .withQueryParam("lang", WireMock.equalTo("en"))
                .withQueryParam("cnt", WireMock.equalTo("4"))
                .withQueryParam("appid", WireMock.equalTo(""))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.OK.value())
                        .withBody(expected)));
        String result = String.valueOf(openWeatherApiService.getWeatherForecastByGeographicCoordinates("0", "0", "json", "metric", "en", "4", ""));

        Assertions.assertEquals(expected, result);
    }
}
