package com.circuit.breaker.v1.circuitbreaker.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class DefaultCircuitBreakerTest {
    private DefaultCircuitBreaker<String> circuitBreaker;

    @BeforeEach
    void setUp() {
        circuitBreaker = new DefaultCircuitBreaker<>(3, 2, Duration.ofMillis(500));
    }

    @Test
    void testCircuitBreakerStartsClosed() {
        assertDoesNotThrow(() -> circuitBreaker.execute(() -> "Success"));
    }

    @Test
    void testCircuitBreakerOpensAfterFailures() {
        Supplier<String> failingSupplier = () -> {
            throw new RuntimeException("Service Down");
        };

        assertThrows(RuntimeException.class, () -> circuitBreaker.execute(failingSupplier));
        assertThrows(RuntimeException.class, () -> circuitBreaker.execute(failingSupplier));
        assertThrows(RuntimeException.class, () -> circuitBreaker.execute(failingSupplier));
        assertThrows(RuntimeException.class, () -> circuitBreaker.execute(() -> "Should be blocked"));
    }

    @Test
    void testCircuitBreakerHalfOpensAfterTimeout() throws InterruptedException {
        Supplier<String> failingSupplier = () -> {
            throw new RuntimeException("Failure");
        };
        for (int i = 0; i < 3; i++) {
            assertThrows(RuntimeException.class, () -> circuitBreaker.execute(failingSupplier));
        }
        Thread.sleep(600);
        assertDoesNotThrow(() -> circuitBreaker.execute(() -> "Success"));
    }

    @Test
    void testCircuitBreakerClosesAfterSuccessInHalfOpen() throws InterruptedException {
        Supplier<String> failingSupplier = () -> {
            throw new RuntimeException("Failure");
        };
        Supplier<String> successSupplier = () -> "Success";
        for (int i = 0; i < 3; i++) {
            assertThrows(RuntimeException.class, () -> circuitBreaker.execute(failingSupplier));
        }
        Thread.sleep(600);
        assertDoesNotThrow(() -> circuitBreaker.execute(successSupplier));
        assertDoesNotThrow(() -> circuitBreaker.execute(successSupplier)); // Esto debería cerrarlo
        assertDoesNotThrow(() -> circuitBreaker.execute(successSupplier));
    }

    @Test
    void testCircuitBreakerReopensIfFailureInHalfOpen() throws InterruptedException {
        Supplier<String> failingSupplier = () -> {
            throw new RuntimeException("Failure");
        };

        for (int i = 0; i < 3; i++) {
            assertThrows(RuntimeException.class, () -> circuitBreaker.execute(failingSupplier));
        }
        Thread.sleep(600);
        assertThrows(RuntimeException.class, () -> circuitBreaker.execute(failingSupplier));
        assertThrows(RuntimeException.class, () -> circuitBreaker.execute(() -> "Should be blocked"));
    }
}