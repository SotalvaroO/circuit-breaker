package com.circuit.breaker.v2.service;

import com.circuit.breaker.TestConfig;
import com.circuit.breaker.v2.service.FooBarServiceV2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = TestConfig.class)
class FooBarServiceV2Test {

    @Autowired
    private FooBarServiceV2 fooBarServiceV2;


    @Test
    void givenASeriesOfInputsThatMustOpenTheCircuit_whenFetchARightRecordWithinFiveSeconds_thenShouldThrowCircuitBreakerException() {
        makeFailureRequest();
        makeFailureRequest();
        makeFailureRequest();
        Assertions.assertEquals("Circuit breaker is open. Please try again later.", makeFailureRequest());
    }

    @Test
    void givenAnOpenCircuitBreaker_whenMakeEnoughSuccessfulRequests_thenShouldCloseTheCircuitBreaker() throws InterruptedException {
        makeFailureRequest();
        makeFailureRequest();
        makeFailureRequest();
        Thread.sleep(10000);
        makeSuccessfulRequest();
        makeSuccessfulRequest();
        makeSuccessfulRequest();
        makeSuccessfulRequest();
        String s = makeSuccessfulRequest();
        Assertions.assertEquals("Data fetched correctly", s);
    }

    private String makeSuccessfulRequest() {
        return fooBarServiceV2.fetchData(1);
    }

    private String makeFailureRequest() {
        return assertThrows(RuntimeException.class,
                () -> fooBarServiceV2.fetchData(0)
        ).getMessage();
    }
}