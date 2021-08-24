package com.example.yangyistarter.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class GreetingServiceTest {
    @MockBean
    private GreetingService greetingService;
    @Test
    public void should_return_hello_when_access_greeting_interface(){
        when(greetingService.hello()).thenReturn("hello, world!");
        assertEquals("hello, world!", greetingService.hello());
    }
}
