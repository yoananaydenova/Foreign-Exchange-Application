package com.yoanan.foreignexchangeapp.service.impl;


import com.yoanan.foreignexchangeapp.model.binding.TransactionBindingModel;
import com.yoanan.foreignexchangeapp.model.entity.TransactionEntity;
import com.yoanan.foreignexchangeapp.model.service.TransactionServiceModel;
import com.yoanan.foreignexchangeapp.repository.TransactionRepository;
import com.yoanan.foreignexchangeapp.service.ExchangeRateClientService;
import com.yoanan.foreignexchangeapp.service.TransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {


    private final TransactionRepository transactionRepository;
    private final ExchangeRateClientService exchangeRateClientService;
    private final ModelMapper modelMapper;

    public TransactionServiceImpl(TransactionRepository transactionRepository, ExchangeRateClientService exchangeRateClientService, ModelMapper modelMapper) {
        this.transactionRepository = transactionRepository;
        this.exchangeRateClientService = exchangeRateClientService;
        this.modelMapper = modelMapper;
    }

    @Override
    public TransactionServiceModel createTransaction(TransactionBindingModel transactionBindingModel) {

        BigDecimal exchangeRate = exchangeRateClientService.getExchangeRate(transactionBindingModel.getSourceCurrency(), transactionBindingModel.getTargetCurrency());

        TransactionEntity newTransaction = modelMapper.map(transactionBindingModel, TransactionEntity.class);
        newTransaction.setDate(LocalDate.now());
        newTransaction.setExchangeRate(exchangeRate);
        newTransaction.setTargetAmount(transactionBindingModel.getSourceAmount().multiply(exchangeRate));

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


