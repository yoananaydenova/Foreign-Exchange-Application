package com.yoanan.foreignexchangeapp.service;

import com.yoanan.foreignexchangeapp.exceptions.ExchangeRateClientFailureException;
import com.yoanan.foreignexchangeapp.exceptions.InvalidCurrencyException;
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

import javax.money.Monetary;
import javax.money.UnknownCurrencyException;
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
        String json = """
                {
                    "result": "success",
                    "documentation": "https://www.exchangerate-api.com/docs",
                    "terms_of_use": "https://www.exchangerate-api.com/terms",
                    "time_last_update_unix": 1753401601,
                    "time_last_update_utc": "Fri, 25 Jul 2025 00:00:01 +0000",
                    "time_next_update_unix": 1753488001,
                    "time_next_update_utc": "Sat, 26 Jul 2025 00:00:01 +0000",
                    "base_code": "EUR",
                    "conversion_rates": {
                        "EUR": 1,
                        "USD": 1.17259,
                        "AFN": 80.9840,
                        "ALL": 97.5553}}""";

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
        String json = """
                {
                    "result": "success",
                    "documentation": "https://www.exchangerate-api.com/docs",
                    "terms_of_use": "https://www.exchangerate-api.com/terms",
                    "time_last_update_unix": 1753401601,
                    "time_last_update_utc": "Fri, 25 Jul 2025 00:00:01 +0000",
                    "time_next_update_unix": 1753488001,
                    "time_next_update_utc": "Sat, 26 Jul 2025 00:00:01 +0000",
                    "base_code": "EUR",
                    "conversion_rates": {
                        "EUR": 1,
                        "USD": 1.17259,
                        "AFN": 80.9840,
                        "ALL": 97.5553}}""";

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
        assertThat(request.getPath()).isEqualTo("/api//latest/EUR");
    }

    @Test
    public void givenCurrencyCode_whenBaseCurrencyNoExist_thanThrowsException() {
        String bgn = "AAA";
        String eur = "EUR";

        assertThrows(UnknownCurrencyException.class, () ->
                exchangeRateClientService.getExchangeRate(bgn, eur)
        );
    }

    @Test
    public void givenCurrencyCode_whenTargetCurrencyNoExist_thanThrowsException() {
        String bgn = "EUR";
        String eur = "BBB";

        assertThrows(UnknownCurrencyException.class, () ->
                exchangeRateClientService.getExchangeRate(bgn, eur)
        );
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

        assertThrows(InvalidCurrencyException.class, () ->
                exchangeRateClientService.getExchangeRate(bgn, eur)
        );

    }
}