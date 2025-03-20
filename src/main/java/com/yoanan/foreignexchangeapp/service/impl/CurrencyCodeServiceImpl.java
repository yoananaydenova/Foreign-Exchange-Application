package com.yoanan.foreignexchangeapp.service.impl;

import com.yoanan.foreignexchangeapp.repository.CurrencyCodeRepository;
import com.yoanan.foreignexchangeapp.service.CurrencyCodeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CurrencyCodeServiceImpl implements CurrencyCodeService {

    private final CurrencyCodeRepository codeRepository;
    private final WebClient webClient;

    @Value("${provider.access-key}")
    private String apiKey;


    public CurrencyCodeServiceImpl(CurrencyCodeRepository codeRepository, WebClient webClient) {
        this.codeRepository = codeRepository;
        this.webClient = webClient;
    }

    @Override
    public void importCurrencyCodes() {

    }
}
