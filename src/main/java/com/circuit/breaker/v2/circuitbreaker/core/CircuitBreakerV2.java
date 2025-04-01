package com.circuit.breaker.v2.circuitbreaker.core;

public interface CircuitBreakerV2<T> {
    boolean canProceed();
    void recordSuccess();
    void recordFailure();
}
