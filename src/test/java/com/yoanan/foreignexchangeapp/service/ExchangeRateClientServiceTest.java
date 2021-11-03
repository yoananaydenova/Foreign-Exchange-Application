package com.yoanan.foreignexchangeapp.service;

import com.yoanan.foreignexchangeapp.exceptions.ExchangeRateClientFailureException;
import com.yoanan.foreignexchangeapp.service.impl.ExchangeRateClientServiceImpl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExchangeRateClientServiceTest {

    private MockWebServer mockWebServer;
    private ExchangeRateClientService exchangeRateClientService;

    @BeforeEach
    void setupMockWebServer() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        String rootUrl = mockWebServer.url("/api/").toString();
        exchangeRateClientService = new ExchangeRateClientServiceImpl(WebClient.create(rootUrl));
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }


    @Test
    void getExchangeRate_whenBaseAndQuoteAreCorrect_shouldReturnCorrectExchangeRate() {
        String json = "{ \"success\": true,\n" +
                "    \"timestamp\": 1631971443,\n" +
                "    \"base\": \"EUR\",\n" +
                "    \"date\": \"2021-09-18\",\n" +
                "    \"rates\": {\n" +
                "        \"USD\": 1.17259\n" +
                "    }}";

        mockWebServer.enqueue(
                new MockResponse().setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(json)
        );

        String eur = "EUR";
        String usd = "USD";

        BigDecimal rate = exchangeRateClientService.getExchangeRate(eur, usd);

        assertThat(rate.doubleValue()).isEqualTo(1.17259);
    }

    @Test
    void getExchangeRate_whenBaseAndQuoteAreCorrect_shouldMakeTheCorrectRequest() throws InterruptedException {
        String json = "{ \"success\": true,\n" +
                "    \"timestamp\": 1631971443,\n" +
                "    \"base\": \"EUR\",\n" +
                "    \"date\": \"2021-09-18\",\n" +
                "    \"rates\": {\n" +
                "        \"USD\": 1.17259\n" +
                "    }}";

        mockWebServer.enqueue(
                new MockResponse().setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(json)
        );

        String eur = "EUR";
        String usd = "USD";

        exchangeRateClientService.getExchangeRate(eur, usd);

        RecordedRequest request = mockWebServer.takeRequest();

        assertThat(request.getMethod()).isEqualTo("GET");
        assertThat(request.getPath()).isEqualTo("/api/latest?access_key=null&base=EUR&symbols=USD");
    }


    @Test
    void getExchangeRate_exchangeError() {
        String json = "{\"success\": false,\n" +
                "    \"error\": {\n" +
                "        \"code\": 105,\n" +
                "        \"type\": \"base_currency_access_restricted\"\n" +
                "    }" +
                "}";

        mockWebServer.enqueue(
                new MockResponse().setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(json)
        );
        String bgn = "BGN";
        String eur = "EUR";

        assertThrows(ExchangeRateClientFailureException.class, () ->
                exchangeRateClientService.getExchangeRate(bgn, eur)
        );

    }
}