package com.circuit.breaker.v2.circuitbreaker.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CircuitBreaker {
    int failureThreshold() default 3;

    int halfOpenThreshold() default 2;

    int resetTimeoutSeconds() default 5;
}
