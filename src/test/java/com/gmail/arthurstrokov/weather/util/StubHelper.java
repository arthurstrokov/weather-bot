package com.gmail.arthurstrokov.weather.util;

import org.springframework.http.HttpStatus;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.gmail.arthurstrokov.weather.util.CommonUtils.readFileAsString;

public class StubHelper {

    public static void stubForPostREST(String url, String requestFilePath, String responseFilePath) {
        stubFor(post(urlEqualTo(url))
                .withRequestBody(equalToJson(readFileAsString(requestFilePath)))
                .willReturn(okJson(readFileAsString(responseFilePath))));
    }

    public static void stubForPostREST(String url, String responseFilePath) {
        stubFor(post(urlEqualTo(url))
                .willReturn(okJson(readFileAsString(responseFilePath))));
    }

    public static void stubForGetREST(String url, String responseFilePath) {
        stubFor(get(urlEqualTo(url))
                .willReturn(okJson(readFileAsString(responseFilePath))));
    }

    public static void stubForGetNotFoundREST(String url, String responseFilePath) {
        stubFor(get(urlEqualTo(url))
                .willReturn(
                        aResponse()
                                .withStatus(404)
                                .withBody(readFileAsString(responseFilePath))));
    }

    public static void stubStatusCode(String uri, HttpStatus status) {
        stubFor(post(uri).willReturn(aResponse()
                .withStatus(status.value())
        ));
    }
}
