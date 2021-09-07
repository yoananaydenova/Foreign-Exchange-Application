package com.yoanan.foreignexchangeapp.service.impl;

import com.yoanan.foreignexchangeapp.model.binding.ProviderBindingModel;
import com.yoanan.foreignexchangeapp.service.ExchangeRateClientService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Map;

@Service
public class ExchangeRateClientServiceImpl implements ExchangeRateClientService {

    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(3);
    private static final String TYPE_ERROR_PROVIDER = "type";

    @Value("${provider.access-key}")
    private String FixerAccessKey;

    private final WebClient webClient;

    public ExchangeRateClientServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public BigDecimal getExchangeRate(String base, String quote) {
        ProviderBindingModel bindingModel = webClient
                .get()
                .uri("/latest?access_key=" + FixerAccessKey + "&base=" + base + "&symbols=" + quote)
                //.accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ProviderBindingModel.class)
                .block(REQUEST_TIMEOUT);


        if (bindingModel == null) {
            throw new NullPointerException("Opppps something is going wrong! Try again letter");
        }

        if (!bindingModel.isState()) {
            //TODO throw custom message
            String message = bindingModel.getError().get(TYPE_ERROR_PROVIDER).replace("_", " ");
            throw new IllegalArgumentException(message.substring(0, 1).toUpperCase() + message.substring(1) + "!");
        }

        return bindingModel.getRates().get(quote);

    }
}
