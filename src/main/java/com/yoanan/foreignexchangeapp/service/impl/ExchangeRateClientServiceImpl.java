package com.yoanan.foreignexchangeapp.service.impl;

import com.yoanan.foreignexchangeapp.exceptions.ExchangeRateClientFailureException;
import com.yoanan.foreignexchangeapp.model.binding.ProviderBindingModel;
import com.yoanan.foreignexchangeapp.service.ExchangeRateClientService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;

@Service
public class ExchangeRateClientServiceImpl implements ExchangeRateClientService {


    @Value("${provider.access-key}")
    private String FixerAccessKey;

    private final WebClient webClient;

    public ExchangeRateClientServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public BigDecimal getExchangeRate(String base, String quote) {
        return webClient
                .get()
                .uri("/latest?access_key=" + FixerAccessKey + "&base=" + base + "&symbols=" + quote)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ProviderBindingModel.class)
                //.block(REQUEST_TIMEOUT)
                .blockOptional()
                .map(response -> {
                    if (!response.isState()) {
                        throw new ExchangeRateClientFailureException();
                    } else {
                        return response.getRates().get(quote);
                    }
                })
                .orElseThrow(ExchangeRateClientFailureException::new);

    }
}
