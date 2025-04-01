package com.circuit.breaker.v1.circuitbreaker.core;

import java.util.function.Supplier;

public interface CircuitBreaker<T> {
    T execute(Supplier<T> supplier);

    void open();

    void close();

    void halfOpen();
}