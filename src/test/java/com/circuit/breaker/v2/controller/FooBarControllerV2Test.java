package com.circuit.breaker.v2.controller;

import com.circuit.breaker.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(classes = TestConfig.class)
@AutoConfigureMockMvc
class FooBarControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getData() {
    }

    @Test
    void shouldReturnDefaultMessage() throws Exception {
        this.mockMvc.perform(get("/api/v2/foo")
                .param("num", "0")
        );
        this.mockMvc.perform(get("/api/v2/foo")
                .param("num", "0")
        );
        this.mockMvc.perform(get("/api/v2/foo")
                .param("num", "0")
        );
        ResultActions num = this.mockMvc.perform(get("/api/v2/foo")
                .param("num", "0")
        );
        ResultActions num2 = this.mockMvc.perform(get("/api/v2/foo")
                .param("num", "0")
        );
    }
}