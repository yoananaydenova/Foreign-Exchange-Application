package com.yoanan.foreignexchangeapp.service.impl;

import com.yoanan.foreignexchangeapp.exceptions.ExchangeRateClientFailureException;
import com.yoanan.foreignexchangeapp.exceptions.InvalidCurrencyException;
import com.yoanan.foreignexchangeapp.model.binding.ProviderBindingModel;
import com.yoanan.foreignexchangeapp.service.ExchangeRateClientService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Set;


@Service
public class ExchangeRateClientServiceImpl implements ExchangeRateClientService {

    //TODO save currencies in db
    private static final Set<String> SUPPORTED_CURRENCIES = Set.of(
            "USD", "EUR", "BGN", "GBP", "JPY"
    );

    @Value("${provider.access-key}")
    private String apiKey;

    private final WebClient webClient;

    public ExchangeRateClientServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public BigDecimal getExchangeRate(String baseCurrency, String targetCurrency) {

        validateCurrencies(baseCurrency, targetCurrency);

        ProviderBindingModel response = webClient.get()
                .uri("/{apiKey}/latest/{base}", apiKey, baseCurrency)
                .retrieve()
                .bodyToMono(ProviderBindingModel.class)
                .blockOptional(Duration.ofSeconds(10))
                .orElseThrow(() -> new ExchangeRateClientFailureException("API timeout"));

        if (!response.isSuccess()) {
            throw new InvalidCurrencyException(
                    "Base currency not supported: " + baseCurrency);
        }

        if (!response.getConversionRates().containsKey(targetCurrency)) {
            throw new InvalidCurrencyException(
                    "Target currency not supported: " + targetCurrency);
        }

        return response.getConversionRates().get(targetCurrency);


    }


    private void validateCurrencies(String base, String target) {
        if (!SUPPORTED_CURRENCIES.contains(base)) {
            throw new InvalidCurrencyException("Unsupported base currency: " + base);
        }
        if (!SUPPORTED_CURRENCIES.contains(target)) {
            throw new InvalidCurrencyException("Unsupported target currency: " + target);
        }
    }
}
