package com.yoanan.foreignexchangeapp.service.impl;

import com.yoanan.foreignexchangeapp.model.binding.ProviderCodeBindingModel;
import com.yoanan.foreignexchangeapp.model.entity.CurrencyCodeEntity;
import com.yoanan.foreignexchangeapp.repository.CurrencyCodeRepository;
import com.yoanan.foreignexchangeapp.service.CurrencyCodeService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CurrencyCodeServiceImpl implements CurrencyCodeService {
    private static final Logger log = LoggerFactory.getLogger(CurrencyCodeServiceImpl.class);

    private final CurrencyCodeRepository currencyCodeRepository;
    private final WebClient webClient;

    @Value("${provider.access-key}")
    private String apiKey;


    public CurrencyCodeServiceImpl(CurrencyCodeRepository codeRepository, WebClient webClient) {
        this.currencyCodeRepository = codeRepository;
        this.webClient = webClient;
    }

    @Override
    @Scheduled(cron = "${currency-codes.sync-cron}") // 1-st day of the month at 00:00
    @PostConstruct
    public void importCurrencyCodes() {
        try {
            ProviderCodeBindingModel response = webClient.get()
                    .uri("https://v6.exchangerate-api.com/v6/{apiKey}/codes", apiKey)
                    .retrieve()
                    .bodyToMono(ProviderCodeBindingModel.class)
                    .block(Duration.ofSeconds(30));

            if (response != null && response.isSuccess()) {
                List<CurrencyCodeEntity> currencies = response.getSupportedCodes().stream()
                        .map(codePair -> new CurrencyCodeEntity(
                                codePair.get(0),
                                codePair.get(1)
                        ))
                        .collect(Collectors.toList());

                currencyCodeRepository.deleteAll();
                currencyCodeRepository.saveAll(currencies);

                log.info("Successfully synced {} currencies", currencies.size());
            }
        } catch (Exception e) {
            log.error("Failed to sync currencies", e);
        }
    }
}
