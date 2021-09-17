package com.yoanan.foreignexchangeapp.controller;

import com.yoanan.foreignexchangeapp.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(TransactionController.class)
@ExtendWith(SpringExtension.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionServiceMock;

    @Test
    void contextLoads() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api")
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn();
    }


    @Test
    void exchange() {
    }

    @Test
    void transaction() {
    }

    @Test
    void transactionsList() {
    }
}