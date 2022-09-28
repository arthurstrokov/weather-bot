package com.gmail.arthurstrokov.weatherbot.controller;

import com.gmail.arthurstrokov.weatherbot.configuration.OpenApiProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {OpenWeatherApiController.class, OpenApiProperties.class})
@ExtendWith(SpringExtension.class)
class OpenWeatherApiControllerTest {

    @Autowired
    private OpenWeatherApiController openWeatherApiController;
    @MockBean
    private RestTemplate restTemplate;

    /**
     * Method under test: {@link OpenWeatherApiController#getCurrentWeatherDataBody()}
     */
    @Test
    void testGetCurrentWeatherDataBody() {
        when(restTemplate.getForEntity((String) any(), (Class<?>) any(), (Object[]) any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/");
        try {
            MockMvcBuilders.standaloneSetup(openWeatherApiController)
                    .build()
                    .perform(requestBuilder)
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                    .andExpect(MockMvcResultMatchers.content()
                            .json("{\"headers\":{},\"body\":null,\"statusCodeValue\":100,\"statusCode\":\"CONTINUE\"}"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
