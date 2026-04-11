package com.example.hogwarts.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Stream;

@RestController
@RequestMapping("/info")
public class InfoController {

    @Value("${server.port:8080}")
    private Integer serverPort;

    @GetMapping("/port")
    public Integer getPort() {
        return serverPort;
    }

    @GetMapping("/sum-parallel")
    public long calculateSumParallel() {
        return Stream.iterate(1, a -> a + 1)
                .parallel()
                .limit(1_000_000)
                .mapToLong(Long::valueOf)
                .sum();
    }
}