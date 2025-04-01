package com.circuit.breaker.v2.service;

import com.circuit.breaker.v2.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

@Service
public class FooBarServiceV2 {

    @CircuitBreaker(failureThreshold = 3, halfOpenThreshold = 5, resetTimeoutSeconds = 10)
    public String fetchData(int number) {
        if (number<1) {
            throw new RuntimeException("External service failed");
        }
        return "Data fetched correctly";
    }

}
