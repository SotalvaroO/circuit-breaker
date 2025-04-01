package com.circuit.breaker.v2.controller;

import com.circuit.breaker.v2.dto.Response;
import com.circuit.breaker.v2.service.FooBarServiceV2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v2")
public class FooBarControllerV2 {

    private final FooBarServiceV2 fooBarService;

    public FooBarControllerV2(FooBarServiceV2 fooBarService) {
        this.fooBarService = fooBarService;
    }

    @GetMapping("/foo")
    public ResponseEntity<Response> getData(@RequestParam("num") int num) {
        try {
            return ResponseEntity.ok(new Response(fooBarService.fetchData(num)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new Response("Error: " + e.getMessage()));
        }
    }

}
