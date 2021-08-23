package com.example.yangyistarter.controller;

import com.example.yangyistarter.service.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
    @Autowired
    private GreetingService greetingService;
    @GetMapping("/hello")
    public String hello(){
        return greetingService.hello();
    }

}
