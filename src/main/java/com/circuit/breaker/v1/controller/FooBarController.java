package com.circuit.breaker.v1.controller;

import com.circuit.breaker.v1.service.FooBarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
public class FooBarController {

    private final FooBarService fooBarService;

    public FooBarController(FooBarService fooBarService) {
        this.fooBarService = fooBarService;
    }

    @GetMapping(value = "/foo")
    public String getFoo() {
        try {
            return fooBarService.fetchData();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping(value = "/bar")
    public ResponseEntity<?> getBar() {
        return ResponseEntity.ok().build();
    }

}
