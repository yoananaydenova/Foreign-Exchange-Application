package com.yoanan.foreignexchangeapp;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ForeignExchangeAppApplicationTests {


    @Test
    void contextLoads()  {
    }


//    @MockBean
//    private TransactionService transactionServiceMock;


//    FOR DOCUMENTATION
//    @Autowired
//    private WebApplicationContext context;
//
//    @Autowired
//    private ObjectMapper objectMapper;


    //  private List<TransactionViewModel> transactions = null;


//    @BeforeEach
//    public void setUp(WebApplicationContext webApplicationContext,
//                      RestDocumentationContextProvider restDocumentation) {
//
//
//        this.mockMvc = MockMvcBuilders
//                .webAppContextSetup(webApplicationContext)
//                .apply(documentationConfiguration(restDocumentation))
//                .alwaysDo(JacksonResultHandlers.prepareJackson(objectMapper))
//                .alwaysDo(MockMvcRestDocumentation.document("{method-name}",
//                        Preprocessors.preprocessRequest(),
//                        Preprocessors.preprocessResponse(
//                                ResponseModifyingPreprocessors.replaceBinaryContent(),
//                                ResponseModifyingPreprocessors.limitJsonArrayLength(objectMapper),
//                                Preprocessors.prettyPrint())))
//                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation)
//                        .uris()
//                        .withScheme("http")
//                        .withHost("localhost")
//                        .withPort(8080)
//                        .and().snippets()
//                        .withDefaults(CliDocumentation.curlRequest(),
//                                HttpDocumentation.httpRequest(),
//                                HttpDocumentation.httpResponse(),
//                                AutoDocumentation.requestFields(),
//                                AutoDocumentation.responseFields(),
//                                AutoDocumentation.pathParameters(),
//                                AutoDocumentation.requestParameters(),
//                                AutoDocumentation.description(),
//                                AutoDocumentation.methodAndPath(),
//                                AutoDocumentation.section()))
//                .build();

    //String id, LocalDate date, String baseCurrency, String quoteCurrency, Double exchangeRate
//        transactions = Stream.of(new TransactionViewModel("abc1", LocalDate.now(), "BGN", "EUR", 1.955830),
//                new TransactionViewModel("abc2", LocalDate.now(), "BGN", "USD", 1.631100),
//                new TransactionViewModel("abc3", LocalDate.now(), "BRL", "CAD", 3.17031))
//                .collect(Collectors.toList());

    //  }


}
