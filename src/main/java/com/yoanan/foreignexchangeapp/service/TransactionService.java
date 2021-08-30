package com.yoanan.foreignexchangeapp.service;

import com.yoanan.foreignexchangeapp.ui.model.binding.TransactionBindingModel;
import com.yoanan.foreignexchangeapp.ui.model.service.ProviderServiceModel;
import com.yoanan.foreignexchangeapp.ui.model.service.TransactionServiceModel;

public interface TransactionService {

    ProviderServiceModel getExchangeRate(String base, String quote);

    TransactionServiceModel createTransaction(TransactionBindingModel transactionBindingModel);
}
