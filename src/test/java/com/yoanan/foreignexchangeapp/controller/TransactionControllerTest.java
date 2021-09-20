package com.yoanan.foreignexchangeapp.controller;

import com.yoanan.foreignexchangeapp.model.binding.TransactionBindingModel;
import com.yoanan.foreignexchangeapp.model.service.TransactionServiceModel;
import com.yoanan.foreignexchangeapp.model.view.TransactionViewModel;
import com.yoanan.foreignexchangeapp.service.ExchangeRateClientService;
import com.yoanan.foreignexchangeapp.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private ExchangeRateClientService exchangeRateClientService;

    @MockBean
    private ModelMapper modelMapper;


    @Test
    void exchange_whenResourcesAreRetrievedCorrect_then200IsReceivedWithCorrectExchangeRate() throws Exception {

        when(exchangeRateClientService.getExchangeRate("BGN", "EUR")).thenReturn(new BigDecimal("1.956671"));

        mockMvc
                .perform(get("http://localhost:8080/api/exchange?base=BGN&quote=EUR")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.exchange_rate").value(1.956671));
    }

    @Test
    void transaction_whenResourcesAreRetrievedCorrect_then200IsReceivedWithCorrectTransactionView() throws Exception {
        BigDecimal sourceAmount = new BigDecimal("10");
        String sourceCurrency = "EUR";
        String targetCurrency = "BGN";


        String id = "a640692e-d557-408b-bf5d-0c4dd66a3fbb";
        LocalDate date = LocalDate.now();
        BigDecimal exchangeRate = new BigDecimal("1.952209");
        BigDecimal targetAmount = new BigDecimal("19.52209");

        TransactionBindingModel transactionBindingModel = new TransactionBindingModel(sourceAmount, sourceCurrency, targetCurrency);
        TransactionServiceModel transactionServiceModel =
                new TransactionServiceModel(id, date, sourceCurrency, targetCurrency, exchangeRate, sourceAmount, targetAmount);
        TransactionViewModel transactionViewModel =
                new TransactionViewModel(id, date.toString(), sourceCurrency, targetCurrency, exchangeRate, sourceAmount, targetAmount);

        when(transactionService.createTransaction(transactionBindingModel)).thenReturn(transactionServiceModel);
        when(modelMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any())).thenReturn(transactionViewModel);


        mockMvc
                .perform(post("http://localhost:8080/api/transaction")
                        .contentType("application/json")
                        .content("{ \"source_amount\" : 10,\n" +
                                "    \"source_currency\" : \"EUR\",\n" +
                                "    \"target_currency\" : \"BGN\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("a640692e-d557-408b-bf5d-0c4dd66a3fbb"))
                .andExpect(jsonPath("$.target_amount").value(19.52209));
    }


    @Test
    public void transactionsList_whenResourcesAreRetrievedPagedWithIdAndDate_then200IsReceivedWithCorrectTransactionView() throws Exception {
        String id = "a640692e-d557-408b-bf5d-0c4dd66a3fbb";
        LocalDate date = LocalDate.now();
        BigDecimal exchangeRate = new BigDecimal("1.952209");
        BigDecimal targetAmount = new BigDecimal("19.52209");
        BigDecimal sourceAmount = new BigDecimal("10");
        String sourceCurrency = "EUR";
        String targetCurrency = "BGN";

        TransactionServiceModel transactionServiceModel =
                new TransactionServiceModel(id, date, sourceCurrency, targetCurrency, exchangeRate, sourceAmount, targetAmount);

        TransactionViewModel transactionViewModel =
                new TransactionViewModel(id, date.toString(), sourceCurrency, targetCurrency, exchangeRate, sourceAmount, targetAmount);

        when(transactionService.getTransactionByIdAndDate(id, date)).thenReturn(transactionServiceModel);
        when(modelMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any())).thenReturn(transactionViewModel);

        mockMvc
                .perform(post("http://localhost:8080/api/transactions?page=1&size=4")
                        .contentType("application/json")
                        .content(" {" + "\"id\" : \"" + id + "\"," +
                                "\"date\" : \"" + date.toString() + "\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(id))
                // .andExpect(jsonPath("$[0].date").value(date))
                .andExpect(jsonPath("$[0].date".toString()).value(date.toString()))
                .andExpect(jsonPath("$[0].source_currency").value(sourceCurrency))
                .andExpect(jsonPath("$[0].target_currency").value(targetCurrency))
                .andExpect(jsonPath("$[0].exchange_rate").value(exchangeRate))
                .andExpect(jsonPath("$[0].source_amount").value(sourceAmount))
                .andExpect(jsonPath("$[0].target_amount").value(targetAmount));
    }
//    @Test
//    public void transactionsList_whenPageOfResourcesAreRetrievedOutOfBounds_then404IsReceived(){
//        String url = getFooURL() + "?page=" + randomNumeric(5) + "&size=2";
//        Response response = RestAssured.get.get(url);
//
//        assertThat(response.getStatusCode(), is(404));
//    }
//    @Test
//    public void transactionsList_givenResourcesExist_whenFirstPageIsRetrieved_thenPageContainsResources(){
//        createResource();
//        Response response = RestAssured.get(paths.getFooURL() + "?page=0&size=2");
//
//        assertFalse(response.body().as(List.class).isEmpty());
//    }
}