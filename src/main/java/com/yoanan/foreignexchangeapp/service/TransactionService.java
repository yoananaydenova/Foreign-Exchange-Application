package com.yoanan.foreignexchangeapp.service;

import com.yoanan.foreignexchangeapp.model.binding.TransactionBindingModel;
import com.yoanan.foreignexchangeapp.model.service.ProviderServiceModel;
import com.yoanan.foreignexchangeapp.model.service.TransactionServiceModel;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface TransactionService {

    ProviderServiceModel getExchangeRate(String base, String quote);

    TransactionServiceModel createTransaction(TransactionBindingModel transactionBindingModel);

    TransactionServiceModel getTransactionById(String transactionId);

    Page<TransactionServiceModel> getTransactionsByDate(LocalDate transactionDate, int page, int size);

    TransactionServiceModel getTransactionByIdAndDate(String transactionId, LocalDate transactionDate);
}
