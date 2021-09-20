package com.yoanan.foreignexchangeapp.service;

import com.yoanan.foreignexchangeapp.model.binding.TransactionBindingModel;
import com.yoanan.foreignexchangeapp.model.entity.TransactionEntity;
import com.yoanan.foreignexchangeapp.model.service.TransactionServiceModel;
import com.yoanan.foreignexchangeapp.repository.TransactionRepository;
import com.yoanan.foreignexchangeapp.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ExchangeRateClientService exchangeRateClientService;

    private ModelMapper modelMapper;
    private TransactionService transactionService;

    @BeforeEach
    void init() {
        modelMapper = new ModelMapper();
        transactionService = new TransactionServiceImpl(transactionRepository, exchangeRateClientService, modelMapper);
    }


    @Test
    void createTransaction_validTransactionData_shouldReturnCorrectTransactionServiceModel() {
        BigDecimal sourceAmount = BigDecimal.valueOf(10.5);
        String sourceCurrency = "EUR";
        String targetCurrency = "BGN";
        BigDecimal exchangeRate = BigDecimal.valueOf(1.960303);

        TransactionBindingModel transactionBindingModel =
                new TransactionBindingModel(sourceAmount, sourceCurrency, targetCurrency);

        when(exchangeRateClientService.getExchangeRate(transactionBindingModel.getSourceCurrency(),
                transactionBindingModel.getTargetCurrency())).thenReturn(exchangeRate);

        when(transactionRepository.saveAndFlush(any())).then(returnsFirstArg());

        TransactionServiceModel transaction = transactionService.createTransaction(transactionBindingModel);

        assertThat(transaction.getDate()).isToday();
        assertThat(transaction.getSourceCurrency()).isEqualTo(sourceCurrency);
        assertThat(transaction.getTargetCurrency()).isEqualTo(targetCurrency);
        assertThat(transaction.getExchangeRate()).isEqualTo(new BigDecimal("1.960303"));
        assertThat(transaction.getSourceAmount()).isEqualTo(sourceAmount);
        assertThat(transaction.getTargetAmount()).isEqualTo(sourceAmount.multiply(exchangeRate));
    }

    @Test
    void getTransactionById_validTransactionId_shouldReturnCorrectTransactionServiceModel() {
        String transactionId = "a640692e-d557-408b-bf5d-0c4dd66a3fbb";
        String sourceCurrency = "EUR";
        String targetCurrency = "BGN";
        BigDecimal exchangeRate = BigDecimal.valueOf(1.960303);
        BigDecimal sourceAmount = BigDecimal.valueOf(10.5);
        BigDecimal targetAmount = sourceAmount.multiply(exchangeRate);

        TransactionEntity transactionEntity =
                new TransactionEntity(
                        transactionId,
                        LocalDate.now(),
                        sourceCurrency,
                        targetCurrency,
                        exchangeRate,
                        sourceAmount,
                        targetAmount
                );

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transactionEntity));

        TransactionServiceModel transaction = transactionService.getTransactionById(transactionId);

        assertThat(transaction.getId()).isEqualTo(transactionId);
        assertThat(transaction.getDate()).isToday();
        assertThat(transaction.getSourceCurrency()).isEqualTo(sourceCurrency);
        assertThat(transaction.getTargetCurrency()).isEqualTo(targetCurrency);
        assertThat(transaction.getExchangeRate()).isEqualTo(new BigDecimal("1.960303"));
        assertThat(transaction.getSourceAmount()).isEqualTo(sourceAmount);
        assertThat(transaction.getTargetAmount()).isEqualTo(sourceAmount.multiply(exchangeRate));

    }


    @Test
    void getTransactionById_NOTValidTransactionId_shouldThrows() {
        String transactionId = "a640692e-d557-408b-bf5d-0c4dd66a3fbb";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> transactionService.getTransactionById(transactionId));

        String expectedMessage = "Transaction with id " + transactionId + " not found!";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void getTransactionsByDate_validTransactionDataWithEmptyDB_shouldReturnCorrectPageTransactionServiceModel() {
        LocalDate transactionDate = LocalDate.now();
        int page = 1;
        int size = 5;
        // Page number start from 1
        Pageable pageable = PageRequest.of(page - 1, size);

        TransactionEntity firstTransaction = new TransactionEntity("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeee1", transactionDate,
                "EUR", "BRL", BigDecimal.valueOf(1.5), BigDecimal.valueOf(1), BigDecimal.valueOf(1.5));
        TransactionEntity secondTransaction = new TransactionEntity("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeee2", transactionDate,
                "EUR", "CAD", BigDecimal.valueOf(2.0), BigDecimal.valueOf(5.5), BigDecimal.valueOf(10.0));
        TransactionEntity thirdTransaction = new TransactionEntity("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeee3", transactionDate,
                "EUR", "BGN", BigDecimal.valueOf(1.9), BigDecimal.valueOf(3.0), BigDecimal.valueOf(5.7));
        List<TransactionEntity> transactionsFromDB = new ArrayList<>(List.of(firstTransaction, secondTransaction, thirdTransaction));

        Page<TransactionEntity> pagedResponse = new PageImpl(transactionsFromDB);
        Mockito.when(transactionRepository.findAllByDate(transactionDate, pageable)).thenReturn(pagedResponse);


        Page<TransactionServiceModel> transactionsByDate = transactionService.getTransactionsByDate(transactionDate, page, size);

        assertThat(transactionsByDate.getContent().size()).isEqualTo(3);
        assertThat(transactionsByDate.getContent().get(0).getId()).isEqualTo("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeee1");
        assertThat(transactionsByDate.getContent().get(0).getDate()).isEqualTo(transactionDate);
        assertThat(transactionsByDate.getContent().get(0).getSourceCurrency()).isEqualTo("EUR");
        assertThat(transactionsByDate.getContent().get(0).getTargetCurrency()).isEqualTo("BRL");
        assertThat(transactionsByDate.getContent().get(0).getExchangeRate()).isEqualTo(new BigDecimal("1.5"));
        assertThat(transactionsByDate.getContent().get(0).getSourceAmount()).isEqualTo(new BigDecimal("1"));
        assertThat(transactionsByDate.getContent().get(0).getTargetAmount()).isEqualTo(new BigDecimal("1.5"));

        assertThat(transactionsByDate.getContent().get(1).getId()).isEqualTo("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeee2");
        assertThat(transactionsByDate.getContent().get(1).getDate()).isEqualTo(transactionDate);
        assertThat(transactionsByDate.getContent().get(1).getSourceCurrency()).isEqualTo("EUR");
        assertThat(transactionsByDate.getContent().get(1).getTargetCurrency()).isEqualTo("CAD");
        assertThat(transactionsByDate.getContent().get(1).getExchangeRate()).isEqualTo(new BigDecimal("2.0"));
        assertThat(transactionsByDate.getContent().get(1).getSourceAmount()).isEqualTo(new BigDecimal("5.5"));
        assertThat(transactionsByDate.getContent().get(1).getTargetAmount()).isEqualTo(new BigDecimal("10.0"));

    }

    @Test
    void getTransactionsByDate_validTransactionDataWithEmptyDB_shouldReturnCorrectPageTransactionServiceModelWithZEROSize() {
        LocalDate transactionDate = LocalDate.now();
        int page = 1;
        int size = 5;
        // Page number start from 1
        Pageable pageable = PageRequest.of(page - 1, size);

        List<TransactionEntity> transactionsFromDB = new ArrayList<>();
        Page<TransactionEntity> pagedResponse = new PageImpl(transactionsFromDB);
        Mockito.when(transactionRepository.findAllByDate(transactionDate, pageable)).thenReturn(pagedResponse);


        Page<TransactionServiceModel> transactionsByDate = transactionService.getTransactionsByDate(transactionDate, page, size);

        assertThat(transactionsByDate.getContent().size()).isEqualTo(0);
    }

    @Test
    void getTransactionByIdAndDate_ValidTransactionIdAndDate_shouldReturnCorrectTransactionServiceModel() {
        String transactionId = "a640692e-d557-408b-bf5d-0c4dd66a3fbb";
        String sourceCurrency = "EUR";
        String targetCurrency = "BGN";
        BigDecimal exchangeRate = BigDecimal.valueOf(1.960303);
        BigDecimal sourceAmount = BigDecimal.valueOf(10.5);
        BigDecimal targetAmount = sourceAmount.multiply(exchangeRate);

        LocalDate transactionDate = LocalDate.now();

        TransactionEntity transactionEntity =
                new TransactionEntity(
                        transactionId,
                        LocalDate.now(),
                        sourceCurrency,
                        targetCurrency,
                        exchangeRate,
                        sourceAmount,
                        targetAmount
                );

        when(transactionRepository.findByIdAndDate(transactionId, transactionDate)).thenReturn(Optional.of(transactionEntity));

        TransactionServiceModel transaction = transactionService.getTransactionByIdAndDate(transactionId, transactionDate);
        assertThat(transaction.getId()).isEqualTo(transactionId);
        assertThat(transaction.getDate()).isToday();
        assertThat(transaction.getSourceCurrency()).isEqualTo(sourceCurrency);
        assertThat(transaction.getTargetCurrency()).isEqualTo(targetCurrency);
        assertThat(transaction.getExchangeRate()).isEqualTo(new BigDecimal("1.960303"));
        assertThat(transaction.getSourceAmount()).isEqualTo(sourceAmount);
        assertThat(transaction.getTargetAmount()).isEqualTo(sourceAmount.multiply(exchangeRate));

    }

    @Test
    void getTransactionByIdAndDate_NOTValidTransactionIdAndDate_shouldThrows() {
        String transactionId = "a640692e-d557-408b-bf5d-0c4dd66a3fbb";
        LocalDate transactionDate = LocalDate.now();

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> transactionService.getTransactionByIdAndDate(transactionId, transactionDate));

        String expectedMessage = "Transaction with id " + transactionId + " from " + transactionDate + " not found!";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}