package com.yoanan.foreignexchangeapp.service.impl;

import com.yoanan.foreignexchangeapp.model.binding.ProviderCodeBindingModel;
import com.yoanan.foreignexchangeapp.model.entity.CurrencyCodeEntity;
import com.yoanan.foreignexchangeapp.repository.CurrencyCodeRepository;
import com.yoanan.foreignexchangeapp.service.CurrencyCodeService;
import com.yoanan.foreignexchangeapp.service.LogService;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyCodeServiceImpl.class);

    private final CurrencyCodeRepository currencyCodeRepository;
    private final WebClient webClient;
    private final LogService logService;

    @Value("${provider.access-key}")
    private String apiKey;


    public CurrencyCodeServiceImpl(CurrencyCodeRepository codeRepository, WebClient webClient, LogService logService) {
        this.currencyCodeRepository = codeRepository;
        this.webClient = webClient;
        this.logService = logService;
    }

    @Scheduled(cron = "${currency-codes.sync-cron}") // 1-st day of the month at 00:00
    public void scheduledCurrencyCodesImport() {
        logService.createLog(
                "Scheduled Job",
                "scheduledCurrencyCodesImport",
                this.getClass().getSimpleName(),
                "Starting currency code sync job"
        );
        try {
            importCurrencyCodes();
        } finally {
            logService.createLog(
                    "Scheduled Job",
                    "scheduledImport",
                    this.getClass().getSimpleName(),
                    "Completed currency code sync job"
            );
        }
    }

    @Override
    @PostConstruct
    public void importCurrencyCodes() {
        String methodName = new Object() {}
                .getClass()
                .getEnclosingMethod()
                .getName();

        String className = this.getClass().getSimpleName();
        String requestUrl = "https://v6.exchangerate-api.com/v6/{apiKey}/codes";

        try {
            ProviderCodeBindingModel response = webClient.get()
                    .uri(requestUrl, apiKey)
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

                String successMessage = """
                        Successfully synced %d currencies""".formatted(currencies.size());

                LOGGER.info(successMessage);
                logService.createLog(
                        requestUrl,
                        methodName,
                        className,
                        successMessage
                );
            }
        } catch (Exception e) {

            String errorMessage = """
                    Failed to sync currencies: %s""".formatted(e.getMessage());

            LOGGER.error(errorMessage, e);

            logService.createLog(
                    requestUrl,
                    methodName,
                    className,
                    errorMessage
            );
        }
    }
}
