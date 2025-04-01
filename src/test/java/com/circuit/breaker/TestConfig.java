package com.circuit.breaker;

import com.circuit.breaker.v2.controller.FooBarControllerV2;
import com.circuit.breaker.v2.service.FooBarServiceV2;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.circuit.breaker")
@ComponentScan(basePackages = "com.circuit.breaker")
public class TestConfig {

    @Bean
    public FooBarServiceV2 fooBarServiceV2() {
        return new FooBarServiceV2();
    }

}
