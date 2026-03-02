package com.gmail.arthurstrokov.weather.gateway;

import com.gmail.arthurstrokov.weather.client.OpenWeatherClient;
import com.gmail.arthurstrokov.weather.configuration.OpenWeatherProperties;
import com.gmail.arthurstrokov.weather.dto.WeatherForecastDTO;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherGatewayTest {

    @Mock
    private OpenWeatherClient openWeatherClient;

    @Mock
    private OpenWeatherProperties openWeatherProperties;

    private WeatherGateway weatherGateway;

    @BeforeEach
    void setUp() {
        weatherGateway = new WeatherGateway(openWeatherClient, openWeatherProperties);
    }

    @Test
    @DisplayName("Should get current weather by city")
    void getCurrentWeatherByCity() {
        // given
        String city = "Minsk";
        String expectedResponse = "{\"temp\": 20}";
        when(openWeatherProperties.getMode()).thenReturn("json");
        when(openWeatherProperties.getUnits()).thenReturn("metric");
        when(openWeatherProperties.getLang()).thenReturn("en");
        when(openWeatherProperties.getOpenApiKey()).thenReturn("test-key");
        when(openWeatherClient.getCurrentWeatherByCity(eq(city), any(), any(), any(), any()))
                .thenReturn(expectedResponse);
        // when
        String result = weatherGateway.getCurrentWeatherByCity(city);
        // then
        assertThat(result).isEqualTo(expectedResponse);
        verify(openWeatherClient).getCurrentWeatherByCity(same(city), eq("json"), eq("metric"), eq("en"), eq("test-key"));
    }

    @Test
    @DisplayName("Should get current weather by coordinates")
    void getCurrentWeatherByGeographicCoordinates() {
        // given
        double lat = 53.9;
        double lon = 27.5667;
        String expectedResponse = "{\"temp\": 18}";
        when(openWeatherProperties.getMode()).thenReturn("json");
        when(openWeatherProperties.getUnits()).thenReturn("metric");
        when(openWeatherProperties.getLang()).thenReturn("en");
        when(openWeatherProperties.getOpenApiKey()).thenReturn("test-key");
        when(openWeatherClient.getCurrentWeatherByGeographicCoordinates(any(), any(), any(), any(), any(), any()))
                .thenReturn(expectedResponse);
        // when
        String result = weatherGateway.getCurrentWeatherByGeographicCoordinates(lat, lon);
        // then
        assertThat(result).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should get weather forecast by city as DTO")
    void getWeatherForecastByCity() {
        // given
        String city = "Minsk";
        WeatherForecastDTO forecastDto = new WeatherForecastDTO();
        forecastDto.setCod("200");
        forecastDto.setCnt(4);
        when(openWeatherProperties.getMode()).thenReturn("json");
        when(openWeatherProperties.getUnits()).thenReturn("metric");
        when(openWeatherProperties.getLang()).thenReturn("en");
        when(openWeatherProperties.getCnt()).thenReturn("4");
        when(openWeatherProperties.getOpenApiKey()).thenReturn("test-key");
        when(openWeatherClient.getWeatherForecastByCity(eq(city), any(), any(), any(), any(), any()))
                .thenReturn(forecastDto);
        // when
        String result = weatherGateway.getWeatherForecastByCity(city);
        // then
        Gson gson = new Gson();
        WeatherForecastDTO parsed = gson.fromJson(result, WeatherForecastDTO.class);
        assertThat(parsed)
                .extracting(WeatherForecastDTO::getCod, WeatherForecastDTO::getCnt)
                .containsExactly("200", 4);
        ArgumentCaptor<String> cityCaptor = ArgumentCaptor.forClass(String.class);
        verify(openWeatherClient).getWeatherForecastByCity(
                cityCaptor.capture(),
                eq("json"),
                eq("metric"),
                eq("en"),
                eq("4"),
                eq("test-key")
        );
        assertThat(cityCaptor.getValue()).isEqualTo(city);
    }

    @Test
    @DisplayName("Should get weather forecast by coordinates")
    void getWeatherForecastByGeographicCoordinates() {
        // given
        double lat = 53.9;
        double lon = 27.5667;
        WeatherForecastDTO forecastDto = new WeatherForecastDTO();
        forecastDto.setCod("200");
        forecastDto.setCnt(4);
        when(openWeatherProperties.getMode()).thenReturn("json");
        when(openWeatherProperties.getUnits()).thenReturn("metric");
        when(openWeatherProperties.getLang()).thenReturn("en");
        when(openWeatherProperties.getCnt()).thenReturn("4");
        when(openWeatherProperties.getOpenApiKey()).thenReturn("test-key");
        when(openWeatherClient.getWeatherForecastByGeographicCoordinates(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(forecastDto);
        // when
        String result = weatherGateway.getWeatherForecastByGeographicCoordinates(lat, lon);
        // then
        assertThat(result)
                .contains("\"cod\"")
                .contains("200");
    }
}
