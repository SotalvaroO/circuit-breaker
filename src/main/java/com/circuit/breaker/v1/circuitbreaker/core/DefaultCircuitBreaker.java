package com.circuit.breaker.v1.circuitbreaker.core;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class DefaultCircuitBreaker<T> implements CircuitBreaker<T> {

    private enum State {CLOSED, OPEN, HALF_OPEN}

    private final int failureThreshold;
    private final int halfOpenThreshold;
    private final Duration resetTimeout;
    private final AtomicInteger failureCount = new AtomicInteger(0);
    private final AtomicInteger successCount = new AtomicInteger(0);
    private volatile State state = State.CLOSED;
    private Instant lastFailureTime = Instant.now();

    public DefaultCircuitBreaker(int failureThreshold, int halfOpenThreshold, Duration resetTimeout) {
        this.failureThreshold = failureThreshold;
        this.halfOpenThreshold = halfOpenThreshold;
        this.resetTimeout = resetTimeout;
    }

    @Override
    public T execute(Supplier<T> supplier) {
        if (state == State.OPEN) {
            if (Instant.now().isAfter(lastFailureTime.plus(resetTimeout))) {
                halfOpen(); // Intentamos reactivar el servicio
            } else {
                throw new RuntimeException("Circuit Breaker activado: Circuit is open, request blocked.");
            }
        }

        try {
            T result = supplier.get();
            recordSuccess();
            return result;
        } catch (Exception e) {
            recordFailure();
            throw new RuntimeException("Circuit Breaker activado: " + e.getMessage());
        }
    }

    @Override
    public void open() {
        state = State.OPEN;
        lastFailureTime = Instant.now();
    }

    @Override
    public void close() {
        failureCount.set(0);
        successCount.set(0);
        state = State.CLOSED;
    }

    @Override
    public void halfOpen() {
        state = State.HALF_OPEN;
        successCount.set(0);
    }

    private void recordSuccess() {
        if (state == State.HALF_OPEN) {
            successCount.incrementAndGet();
            if (successCount.get() >= halfOpenThreshold) {
                close();
            }
        } else {
            reset();
        }
    }

    private void recordFailure() {
        failureCount.incrementAndGet();
        lastFailureTime = Instant.now();

        if (state == State.HALF_OPEN) {
            open();
        } else if (failureCount.get() >= failureThreshold) {
            open();
        }
    }

    private void reset() {
        close();
    }
}
