package com.gmail.arthurstrokov.weatherbot.gateway;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.gmail.arthurstrokov.weatherbot.service.OpenWeatherApiService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

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
    public static WireMockServer wireMockServer = new WireMockServer(options().port(8888));
    @Autowired
    OpenWeatherApiService openWeatherApiService;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("https://api.openweathermap.org/data/2.5", wireMockServer::baseUrl);
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

    String expected = "{\n" +
            "  \"cod\": \"200\",\n" +
            "  \"message\": 0,\n" +
            "  \"cnt\": 4,\n" +
            "  \"list\": [\n" +
            "    {\n" +
            "      \"dt\": 1664917200,\n" +
            "      \"main\": {\n" +
            "        \"temp\": 7.86,\n" +
            "        \"feels_like\": 4.38,\n" +
            "        \"temp_min\": 6.29,\n" +
            "        \"temp_max\": 7.86,\n" +
            "        \"pressure\": 1014,\n" +
            "        \"sea_level\": 1014,\n" +
            "        \"grnd_level\": 987,\n" +
            "        \"humidity\": 86,\n" +
            "        \"temp_kf\": 1.57\n" +
            "      },\n" +
            "      \"weather\": [\n" +
            "        {\n" +
            "          \"id\": 803,\n" +
            "          \"main\": \"Clouds\",\n" +
            "          \"description\": \"broken clouds\",\n" +
            "          \"icon\": \"04n\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"clouds\": {\n" +
            "        \"all\": 53\n" +
            "      },\n" +
            "      \"wind\": {\n" +
            "        \"speed\": 6.42,\n" +
            "        \"deg\": 296,\n" +
            "        \"gust\": 14.4\n" +
            "      },\n" +
            "      \"visibility\": 10000,\n" +
            "      \"pop\": 0.01,\n" +
            "      \"sys\": {\n" +
            "        \"pod\": \"n\"\n" +
            "      },\n" +
            "      \"dt_txt\": \"2022-10-04 21:00:00\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"dt\": 1664928000,\n" +
            "      \"main\": {\n" +
            "        \"temp\": 7.09,\n" +
            "        \"feels_like\": 3.89,\n" +
            "        \"temp_min\": 5.56,\n" +
            "        \"temp_max\": 7.09,\n" +
            "        \"pressure\": 1015,\n" +
            "        \"sea_level\": 1015,\n" +
            "        \"grnd_level\": 989,\n" +
            "        \"humidity\": 89,\n" +
            "        \"temp_kf\": 1.53\n" +
            "      },\n" +
            "      \"weather\": [\n" +
            "        {\n" +
            "          \"id\": 802,\n" +
            "          \"main\": \"Clouds\",\n" +
            "          \"description\": \"scattered clouds\",\n" +
            "          \"icon\": \"03n\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"clouds\": {\n" +
            "        \"all\": 47\n" +
            "      },\n" +
            "      \"wind\": {\n" +
            "        \"speed\": 5.15,\n" +
            "        \"deg\": 293,\n" +
            "        \"gust\": 12.09\n" +
            "      },\n" +
            "      \"visibility\": 10000,\n" +
            "      \"pop\": 0,\n" +
            "      \"sys\": {\n" +
            "        \"pod\": \"n\"\n" +
            "      },\n" +
            "      \"dt_txt\": \"2022-10-05 00:00:00\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"dt\": 1664938800,\n" +
            "      \"main\": {\n" +
            "        \"temp\": 7.35,\n" +
            "        \"feels_like\": 4.25,\n" +
            "        \"temp_min\": 7.09,\n" +
            "        \"temp_max\": 7.35,\n" +
            "        \"pressure\": 1016,\n" +
            "        \"sea_level\": 1016,\n" +
            "        \"grnd_level\": 991,\n" +
            "        \"humidity\": 92,\n" +
            "        \"temp_kf\": 0.26\n" +
            "      },\n" +
            "      \"weather\": [\n" +
            "        {\n" +
            "          \"id\": 803,\n" +
            "          \"main\": \"Clouds\",\n" +
            "          \"description\": \"broken clouds\",\n" +
            "          \"icon\": \"04n\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"clouds\": {\n" +
            "        \"all\": 72\n" +
            "      },\n" +
            "      \"wind\": {\n" +
            "        \"speed\": 5.06,\n" +
            "        \"deg\": 290,\n" +
            "        \"gust\": 11.27\n" +
            "      },\n" +
            "      \"visibility\": 10000,\n" +
            "      \"pop\": 0,\n" +
            "      \"sys\": {\n" +
            "        \"pod\": \"n\"\n" +
            "      },\n" +
            "      \"dt_txt\": \"2022-10-05 03:00:00\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"dt\": 1664949600,\n" +
            "      \"main\": {\n" +
            "        \"temp\": 7,\n" +
            "        \"feels_like\": 4.25,\n" +
            "        \"temp_min\": 7,\n" +
            "        \"temp_max\": 7,\n" +
            "        \"pressure\": 1019,\n" +
            "        \"sea_level\": 1019,\n" +
            "        \"grnd_level\": 992,\n" +
            "        \"humidity\": 93,\n" +
            "        \"temp_kf\": 0\n" +
            "      },\n" +
            "      \"weather\": [\n" +
            "        {\n" +
            "          \"id\": 804,\n" +
            "          \"main\": \"Clouds\",\n" +
            "          \"description\": \"overcast clouds\",\n" +
            "          \"icon\": \"04d\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"clouds\": {\n" +
            "        \"all\": 90\n" +
            "      },\n" +
            "      \"wind\": {\n" +
            "        \"speed\": 4.13,\n" +
            "        \"deg\": 273,\n" +
            "        \"gust\": 9.17\n" +
            "      },\n" +
            "      \"visibility\": 10000,\n" +
            "      \"pop\": 0,\n" +
            "      \"sys\": {\n" +
            "        \"pod\": \"d\"\n" +
            "      },\n" +
            "      \"dt_txt\": \"2022-10-05 06:00:00\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"city\": {\n" +
            "    \"id\": 625144,\n" +
            "    \"name\": \"Minsk\",\n" +
            "    \"coord\": {\n" +
            "      \"lat\": 53.9,\n" +
            "      \"lon\": 27.5667\n" +
            "    },\n" +
            "    \"country\": \"BY\",\n" +
            "    \"population\": 1742124,\n" +
            "    \"timezone\": 10800,\n" +
            "    \"sunrise\": 1664856983,\n" +
            "    \"sunset\": 1664898027\n" +
            "  }\n" +
            "}";
}
