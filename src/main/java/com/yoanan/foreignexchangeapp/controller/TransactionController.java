package com.yoanan.foreignexchangeapp.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.yoanan.foreignexchangeapp.model.binding.TransactionBindingModel;
import com.yoanan.foreignexchangeapp.model.binding.TransactionListBindingModel;
import com.yoanan.foreignexchangeapp.model.service.TransactionServiceModel;
import com.yoanan.foreignexchangeapp.model.view.ProviderViewModel;
import com.yoanan.foreignexchangeapp.model.view.TransactionViewModel;
import com.yoanan.foreignexchangeapp.model.view.Views;
import com.yoanan.foreignexchangeapp.service.ExchangeRateClientService;
import com.yoanan.foreignexchangeapp.service.TransactionService;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/api")
public class TransactionController {

    private final TransactionService transactionService;
    private final ExchangeRateClientService exchangeRateClientService;
    private final ModelMapper modelMapper;

    @Autowired
    public TransactionController(TransactionService transactionService, ExchangeRateClientService exchangeRateClientService, ModelMapper modelMapper) {
        this.transactionService = transactionService;
        this.exchangeRateClientService = exchangeRateClientService;
        this.modelMapper = modelMapper;
    }

    //TODO Unit Tests
    //TODO Logging mechanism for errors
    //TODO API Documentation

    // http://localhost:8080/api/exchange?base=EUR&quote=BGN
    @GetMapping("/exchange")
    public ResponseEntity<ProviderViewModel> exchange(@RequestParam(value = "base") @NotBlank @Length(min = 3, max = 3, message = "Base currency must be 3 symbols") String base,
                                                      @RequestParam(value = "quote") @NotBlank @Length(min = 3, max = 3, message = "Quote currency must be 3 symbols") String quote) {

        BigDecimal exchangeRate = exchangeRateClientService.getExchangeRate(base, quote);

        ProviderViewModel toReturn = new ProviderViewModel(exchangeRate);

        return new ResponseEntity<ProviderViewModel>(toReturn, HttpStatus.OK);
    }

//          http://localhost:8080/api/transaction
//           { "source_amount" : 10,
//            "source_currency" : "EUR",
//            "target_currency" : "BGN"}

    @JsonView(Views.TransactionView.class)
    @PostMapping("/transaction")
    public ResponseEntity<TransactionViewModel> transaction(@Valid @RequestBody TransactionBindingModel transactionBindingModel) {

        TransactionServiceModel transactionServiceModel = transactionService.createTransaction(transactionBindingModel);
        TransactionViewModel transactionViewModel = modelMapper.map(transactionServiceModel, TransactionViewModel.class);

        return new ResponseEntity<TransactionViewModel>(transactionViewModel, HttpStatus.OK);

    }

    // http://localhost:8080/api/transactions?page=1&size=4
    //        { "id" : ..........,
    //          "date" : .............}
//    @Pattern(regexp="^(0|[1-9][0-9]*)$", message = "Page number must be greater than 0 and number type!")
//    @Pattern(regexp="^(0|[1-9][0-9]*)$",message = "Size must be greater than 0 and number type!")
    @PostMapping("/transactions")
    public ResponseEntity<List<TransactionViewModel>> transactionsList(@RequestParam(value = "page", defaultValue = "1") String page,
                                                                       @RequestParam(value = "size", defaultValue = "3") String size,
                                                                       @Valid @RequestBody TransactionListBindingModel transactionListBindingModel) {
        int pageInt;
        int sizeInt;
        try {
            pageInt = Integer.parseInt(page);
            sizeInt = Integer.parseInt(size);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Page number and size must be number format!");
        }

        if (pageInt < 1) {
            throw new IllegalArgumentException("Page number must be greater than 0!");
        }

        if (sizeInt < 1) {
            throw new IllegalArgumentException("Size must be greater than 0!");
        }

        String transactionId = transactionListBindingModel.getId();
        LocalDate transactionDate = transactionListBindingModel.getDate();

        // NOT having any data -> throw
        if (transactionId == null && transactionDate == null) {
            throw new IllegalArgumentException("Transaction Id or/and Transaction Date must persist!");
        }


        System.out.println();
        List<TransactionViewModel> result = new ArrayList<>();

        // have ID and DATE -> ONE transaction
        if (transactionId != null && transactionDate != null) {
            TransactionServiceModel transactionByIdAndDate = transactionService.getTransactionByIdAndDate(transactionId, transactionDate);

            result.add(modelMapper.map(transactionByIdAndDate, TransactionViewModel.class));
            //return respond with empty list with 200 OK http status
        }

        // have only ID -> ONE transaction
        if (transactionId != null && transactionDate == null) {
            TransactionServiceModel transactionById = transactionService.getTransactionById(transactionId);
            result.add(modelMapper.map(transactionById, TransactionViewModel.class));
        }


        // have only DATE -> LIST with transactions
        if (transactionId == null && transactionDate != null) {

            Page<TransactionServiceModel> transactionsByDate = transactionService.getTransactionsByDate(transactionDate, pageInt, sizeInt);

            if (pageInt > transactionsByDate.getTotalPages()) {
               // throw new IllegalArgumentException("For page " + pageInt + " there is no records for date "+transactionDate+"!");
                throw new ResourceNotFoundException("For page " + pageInt + " there is no records for date "+transactionDate+"!");
            }

            List<TransactionViewModel> coursesViewModels = transactionsByDate
                    .getContent()
                    .stream()
                    .map(t -> modelMapper.map(t, TransactionViewModel.class))
                    .collect(Collectors.toList());

            result.addAll(coursesViewModels);

        }
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

}
