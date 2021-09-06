package com.yoanan.foreignexchangeapp;


import capital.scalable.restdocs.AutoDocumentation;
import capital.scalable.restdocs.jackson.JacksonResultHandlers;
import capital.scalable.restdocs.response.ResponseModifyingPreprocessors;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoanan.foreignexchangeapp.service.TransactionService;
import com.yoanan.foreignexchangeapp.model.service.ProviderServiceModel;
import com.yoanan.foreignexchangeapp.model.view.TransactionViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.cli.CliDocumentation;
import org.springframework.restdocs.http.HttpDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
class ForeignExchangeAppApplicationTests {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private TransactionService transactionServiceMock;

    private MockMvc mockMvc;

    private List<TransactionViewModel> transactions = null;


    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {


        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(JacksonResultHandlers.prepareJackson(objectMapper))
                .alwaysDo(MockMvcRestDocumentation.document("{method-name}",
                        Preprocessors.preprocessRequest(),
                        Preprocessors.preprocessResponse(
                                ResponseModifyingPreprocessors.replaceBinaryContent(),
                                ResponseModifyingPreprocessors.limitJsonArrayLength(objectMapper),
                                Preprocessors.prettyPrint())))
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation)
                        .uris()
                        .withScheme("http")
                        .withHost("localhost")
                        .withPort(8080)
                        .and().snippets()
                        .withDefaults(CliDocumentation.curlRequest(),
                                HttpDocumentation.httpRequest(),
                                HttpDocumentation.httpResponse(),
                                AutoDocumentation.requestFields(),
                                AutoDocumentation.responseFields(),
                                AutoDocumentation.pathParameters(),
                                AutoDocumentation.requestParameters(),
                                AutoDocumentation.description(),
                                AutoDocumentation.methodAndPath(),
                                AutoDocumentation.section()))
                .build();

        //String id, LocalDate date, String baseCurrency, String quoteCurrency, Double exchangeRate
//        transactions = Stream.of(new TransactionViewModel("abc1", LocalDate.now(), "BGN", "EUR", 1.955830),
//                new TransactionViewModel("abc2", LocalDate.now(), "BGN", "USD", 1.631100),
//                new TransactionViewModel("abc3", LocalDate.now(), "BRL", "CAD", 3.17031))
//                .collect(Collectors.toList());

    }

    @Test
    void contextLoads() {
    }

    @Test
    public void whenResourcesAreRetrievedCorrect_then200IsReceivedWithCorrectExchangeRate() throws Exception {
        ProviderServiceModel providerServiceModel = new ProviderServiceModel(LocalDate.now(), "EUR", "BGN", 1.956671);
        when(transactionServiceMock.getExchangeRate("BGN", "EUR")).thenReturn(providerServiceModel);

        mockMvc
                .perform(get("http://localhost:8080/api/exchange?base=EUR&quote=BGN")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exchange_rate").value(1.956671));
    }

//    @Test
//    public void whenSourceCurrencyIsNotRetrieved_then200IsReceivedWithCorrectExchangeRate() throws Exception {
//
//        ProviderServiceModel providerServiceModel = new ProviderServiceModel(LocalDate.now(), "EUR", "BGN", 1.956671);
//        when(transactionServiceMock.getExchangeRate("BGN", "EUR")).thenReturn(providerServiceModel);
//
//        mockMvc
//                .perform(get("http://localhost:8080/api/exchange?base=EUR&quote=BGN")
//                        .contentType("application/json"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.exchange_rate").value(1.956671));
//
//    }



//    @Test
//    public void whenResourcesAreRetrievedPaged_then200IsReceived(){
//        Response response = RestAssured.get(paths.getFooURL() + "?page=0&size=2");
//
//        assertThat(response.getStatusCode(), is(200));
//    }
//    @Test
//    public void whenPageOfResourcesAreRetrievedOutOfBounds_then404IsReceived(){
//        String url = getFooURL() + "?page=" + randomNumeric(5) + "&size=2";
//        Response response = RestAssured.get.get(url);
//
//        assertThat(response.getStatusCode(), is(404));
//    }
//    @Test
//    public void givenResourcesExist_whenFirstPageIsRetrieved_thenPageContainsResources(){
//        createResource();
//        Response response = RestAssured.get(paths.getFooURL() + "?page=0&size=2");
//
//        assertFalse(response.body().as(List.class).isEmpty());
//    }

}
