package com.example.yangyistarter.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class GreetingService {

    public String hello() {
        return "hello, world!";
    }
}
