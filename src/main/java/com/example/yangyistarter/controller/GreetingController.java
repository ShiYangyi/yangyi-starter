package com.example.yangyistarter.controller;

import com.example.yangyistarter.service.GreetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GreetingController {
    private final GreetingService greetingService;

    @GetMapping("/hello")
    public String hello() {
        return greetingService.hello();
    }
}
