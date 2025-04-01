package com.circuit.breaker.v2.circuitbreaker.annotation;

import com.circuit.breaker.v2.circuitbreaker.core.CircuitBreakerV2;
import com.circuit.breaker.v2.circuitbreaker.core.DefaultCircuitBreakerV2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class CircuitBreakerAspect {

    private final Map<String, CircuitBreakerV2<Object>> circuitBreakers = new ConcurrentHashMap<>();

    @Around("@annotation(circuitBreaker)")
    public Object applyCircuitBreaker(ProceedingJoinPoint joinPoint, CircuitBreaker circuitBreaker) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        CircuitBreakerV2<Object> cb = circuitBreakers.computeIfAbsent(
                methodName,
                key -> new DefaultCircuitBreakerV2<>(
                        circuitBreaker.failureThreshold(),
                        circuitBreaker.halfOpenThreshold(),
                        Duration.ofSeconds(circuitBreaker.resetTimeoutSeconds()))
        );

        if (!cb.canProceed()) {
            throw new RuntimeException("Circuit breaker is open. Please try again later.");
        }

        try {

            Object result = joinPoint.proceed();
            cb.recordSuccess();
            return result;
        } catch (Exception e) {
            cb.recordFailure();
            throw e;
        }
    }
}
