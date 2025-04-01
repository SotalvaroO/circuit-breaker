package com.circuit.breaker.v1.service;

import com.circuit.breaker.v1.circuitbreaker.core.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class FooBarService {
    private final CircuitBreaker<String> circuitBreaker;

    public FooBarService(CircuitBreaker<String> circuitBreaker) {
        this.circuitBreaker = circuitBreaker;
    }

    public String fetchData() {
        return circuitBreaker.execute(() -> {
            if (new Random().nextInt(10) < 6) {
                throw new RuntimeException("Fallo en el servicio externo");
            }
            return "Datos recibidos correctamente!";
        });
    }
}
