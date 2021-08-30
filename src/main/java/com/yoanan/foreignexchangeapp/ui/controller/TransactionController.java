package com.yoanan.foreignexchangeapp.ui.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.yoanan.foreignexchangeapp.service.TransactionService;
import com.yoanan.foreignexchangeapp.ui.model.binding.TransactionBindingModel;
import com.yoanan.foreignexchangeapp.ui.model.service.ProviderServiceModel;
import com.yoanan.foreignexchangeapp.ui.model.service.TransactionServiceModel;
import com.yoanan.foreignexchangeapp.ui.model.view.ProviderViewModel;
import com.yoanan.foreignexchangeapp.ui.model.view.TransactionViewModel;
import com.yoanan.foreignexchangeapp.ui.model.view.Views;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class TransactionController {

    private final TransactionService transactionService;
    private final ModelMapper modelMapper;

    @Autowired
    public TransactionController(TransactionService transactionService, ModelMapper modelMapper) {
        this.transactionService = transactionService;
        this.modelMapper = modelMapper;
    }

    // http://localhost:8080/api/exchange?base=EUR&quote=BGN
    @GetMapping("/exchange")
    public ResponseEntity<ProviderViewModel> exchange(@RequestParam(value = "base") String base,
                                                      @RequestParam(value = "quote") String quote) {

        ProviderServiceModel providerServiceModel = transactionService.getExchangeRate(base, quote);

        ProviderViewModel toReturn = modelMapper.map(providerServiceModel, ProviderViewModel.class);

        return new ResponseEntity<ProviderViewModel>(toReturn, HttpStatus.OK);
    }

    // http://localhost:8080/api/conversion
//           { "source_amount" : 10,
//            "source_currency" : "EUR",
//            "target_currency" : "BGN"}

    @JsonView(Views.TransactionView.class)
    @PostMapping("/conversion")
    public ResponseEntity<TransactionViewModel> conversion(@RequestBody TransactionBindingModel transactionBindingModel) {

        TransactionServiceModel transactionServiceModel = transactionService.createTransaction(transactionBindingModel);
        TransactionViewModel transactionViewModel = modelMapper.map(transactionServiceModel, TransactionViewModel.class);

        return new ResponseEntity<TransactionViewModel>(transactionViewModel, HttpStatus.OK);

    }

}
