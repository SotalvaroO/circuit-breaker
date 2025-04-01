package com.circuit.breaker.v2.circuitbreaker.core;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

public class DefaultCircuitBreakerV2<T> implements CircuitBreakerV2<T> {
    private static final Logger logger = Logger.getLogger(DefaultCircuitBreakerV2.class.getName());

    private enum State {CLOSED, OPEN, HALF_OPEN}

    private final int failureThreshold;
    private final int halfOpenThreshold;
    private final Duration resetTimeout;

    private final AtomicReference<State> state = new AtomicReference<>(State.CLOSED);
    private final AtomicInteger failureCount = new AtomicInteger(0);
    private final AtomicInteger successCount = new AtomicInteger(0);
    private Instant lastFailureTime = Instant.ofEpochMilli(0);

    public DefaultCircuitBreakerV2(int failureThreshold, int halfOpenThreshold, Duration resetTimeout) {
        this.failureThreshold = failureThreshold;
        this.halfOpenThreshold = halfOpenThreshold;
        this.resetTimeout = resetTimeout;
    }

    @Override
    public boolean canProceed() {
        State currentState = state.get();
        if (currentState == State.OPEN) {
            if (Instant.now().isAfter(lastFailureTime.plus(resetTimeout))) {
                if (state.compareAndSet(State.OPEN, State.HALF_OPEN)) {
                    logger.info("Circuit Breaker moving to HALF-OPEN state");
                    successCount.set(0);
                }
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public void recordFailure() {
        State currentState = state.get();
        if (currentState == State.CLOSED || currentState == State.HALF_OPEN) {
            int failures = failureCount.incrementAndGet();
            if (failures >= failureThreshold) {
                if (state.compareAndSet(currentState, State.OPEN)) {
                    lastFailureTime = Instant.now();
                    logger.warning("Circuit Breaker OPEN! Requests will be blocked.");
                }
            }
        }
    }

    @Override
    public void recordSuccess() {

        if (state.get() == State.HALF_OPEN) {
            int successes = successCount.incrementAndGet();
            if (successes >= halfOpenThreshold) {
                if (state.compareAndSet(State.HALF_OPEN, State.CLOSED)) {
                    logger.info("Circuit Breaker CLOSED! Normal operation resumed.");
                    failureCount.set(0);
                }
            }
        }
    }
}
