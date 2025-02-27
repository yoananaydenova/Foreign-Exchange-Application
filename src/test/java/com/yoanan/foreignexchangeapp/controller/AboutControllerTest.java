package com.yoanan.foreignexchangeapp.controller;

import com.yoanan.foreignexchangeapp.config.ConfigProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AboutController.class)
public class AboutControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConfigProperties configProperties;

    @Test
    void getAbout_shouldReturnApiInfo() throws Exception {
        String expectedApiUrl = "https://api.example.com";
        String expectedApiVersion = "v1";

        when(configProperties.apiUrl()).thenReturn(expectedApiUrl);
        when(configProperties.apiVersion()).thenReturn(expectedApiVersion);

        mockMvc.perform(get("/about"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.apiUrl").value(expectedApiUrl))
                .andExpect(jsonPath("$.apiVersion").value(expectedApiVersion));
    }

    @Test
    void getAbout_shouldReturn404ForWrongPath() throws Exception {
        mockMvc.perform(get("/about/wrong"))
                .andExpect(status().isNotFound());
    }
}
