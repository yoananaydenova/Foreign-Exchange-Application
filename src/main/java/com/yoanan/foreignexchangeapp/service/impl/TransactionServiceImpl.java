package com.yoanan.foreignexchangeapp.service.impl;

import com.yoanan.foreignexchangeapp.repository.TransactionRepository;
import com.yoanan.foreignexchangeapp.service.TransactionService;
import com.yoanan.foreignexchangeapp.ui.model.binding.TransactionBindingModel;
import com.yoanan.foreignexchangeapp.ui.model.binding.ProviderBindingModel;
import com.yoanan.foreignexchangeapp.ui.model.entity.TransactionEntity;
import com.yoanan.foreignexchangeapp.ui.model.service.ProviderServiceModel;
import com.yoanan.foreignexchangeapp.ui.model.service.TransactionServiceModel;
import org.hibernate.Transaction;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(5);

    private final TransactionRepository transactionRepository;
    private final WebClient webClient;
    private final ModelMapper modelMapper;

    public TransactionServiceImpl(TransactionRepository transactionRepository, WebClient webClient, ModelMapper modelMapper) {
        this.transactionRepository = transactionRepository;
        this.webClient = webClient;
        this.modelMapper = modelMapper;
    }


    @Override
    public ProviderServiceModel getExchangeRate(String base, String quote) {
        ProviderBindingModel bindingModel = webClient
                .get()
                .uri("/latest?access_key=5da969c91399bfb1bce113238280acb2&base="+base+"&symbols="+quote)
                //.accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ProviderBindingModel.class)
                .block(REQUEST_TIMEOUT);

        ProviderServiceModel providerServiceModel = modelMapper.map(bindingModel, ProviderServiceModel.class);
        Map.Entry<String, Double> entry = bindingModel.getRates().entrySet().iterator().next();
        providerServiceModel.setQuoteCurrency(entry.getKey());
        providerServiceModel.setExchangeRate(entry.getValue());

        return providerServiceModel;
    }

    @Override
    public TransactionServiceModel createTransaction(TransactionBindingModel transactionBindingModel) {

        ProviderServiceModel providerServiceModel = getExchangeRate(transactionBindingModel.getSourceCurrency(), transactionBindingModel.getTargetCurrency());

        BigDecimal targetAmount = transactionBindingModel.getSourceAmount().multiply(BigDecimal.valueOf(providerServiceModel.getExchangeRate()));

        TransactionEntity newTransaction =
                new TransactionEntity(null,
                        providerServiceModel.getDate(),
                        providerServiceModel.getBaseCurrency(),
                        providerServiceModel.getQuoteCurrency(),
                        providerServiceModel.getExchangeRate(),
                        transactionBindingModel.getSourceAmount(),
                        targetAmount);

        TransactionEntity transactionEntity = transactionRepository.saveAndFlush(newTransaction);

        return modelMapper.map(transactionEntity, TransactionServiceModel.class);
    }
}


