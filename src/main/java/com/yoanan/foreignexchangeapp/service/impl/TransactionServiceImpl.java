package com.yoanan.foreignexchangeapp.service.impl;

import com.yoanan.foreignexchangeapp.repository.TransactionRepository;
import com.yoanan.foreignexchangeapp.service.TransactionService;
import com.yoanan.foreignexchangeapp.model.binding.ProviderBindingModel;
import com.yoanan.foreignexchangeapp.model.binding.TransactionBindingModel;
import com.yoanan.foreignexchangeapp.model.entity.TransactionEntity;
import com.yoanan.foreignexchangeapp.model.service.ProviderServiceModel;
import com.yoanan.foreignexchangeapp.model.service.TransactionServiceModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(3);
    private static final String TYPE_ERROR_PROVIDER = "type";

    @Value("${provider.access-key}")
    private String FixerAccessKey;

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
                .uri("/latest?access_key=" + FixerAccessKey + "&base=" + base + "&symbols=" + quote)
                //.accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ProviderBindingModel.class)
                .block(REQUEST_TIMEOUT);


        if (bindingModel == null) {
            throw new NullPointerException("Opppps something was wrong! Try again letter");
        }

        if (!bindingModel.isState()) {
            String message = bindingModel.getError().get(TYPE_ERROR_PROVIDER).replace("_", " ");
            throw new IllegalArgumentException(message.substring(0, 1).toUpperCase() + message.substring(1) + "!");
        }
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

    @Override
    public TransactionServiceModel getTransactionById(String transactionId) {
        TransactionEntity transactionEntityById = transactionRepository.findById(transactionId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Transaction with id " + transactionId + " not found!"));
        return modelMapper.map(transactionEntityById, TransactionServiceModel.class);
    }

    @Override
    public Page<TransactionServiceModel> getTransactionsByDate(LocalDate transactionDate, int page, int size) {

        Pageable pageable = PageRequest.of(page - 1, size);

        Page<TransactionEntity> transactionPage = transactionRepository
                .findAllByDate(transactionDate, pageable);

        int totalElements = (int) transactionPage.getTotalElements();

        return new PageImpl<TransactionServiceModel>(transactionPage
                .stream()
                .map(course -> modelMapper.map(course, TransactionServiceModel.class))
                .collect(Collectors.toList()), pageable, totalElements);
    }

    @Override
    public TransactionServiceModel getTransactionByIdAndDate(String transactionId, LocalDate transactionDate) {

        TransactionEntity transactionEntityByIdAndDate = transactionRepository.findByIdAndDate(transactionId, transactionDate)
                .orElseThrow(() ->
                        new IllegalArgumentException("Transaction with id " + transactionId + " from " + transactionDate + " not found!"));

        return modelMapper.map(transactionEntityByIdAndDate, TransactionServiceModel.class);
    }


}


