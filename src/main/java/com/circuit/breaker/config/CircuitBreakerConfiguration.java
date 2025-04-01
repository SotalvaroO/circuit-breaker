package com.circuit.breaker.config;

import com.circuit.breaker.v1.circuitbreaker.core.CircuitBreaker;
import com.circuit.breaker.v1.circuitbreaker.core.DefaultCircuitBreaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CircuitBreakerConfiguration {

    @Bean
    public <T> CircuitBreaker<T> circuitBreaker() {
        return new DefaultCircuitBreaker<>(3, 2, Duration.ofSeconds(5));
    }

}
