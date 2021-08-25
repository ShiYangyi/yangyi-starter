package com.example.yangyistarter.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class GreetingServiceTest {
    @MockBean
    private GreetingService greetingService;

    @Test
    public void should_return_hello_when_access_greeting_interface() {
        when(greetingService.hello()).thenReturn("hello, world!");
        assertEquals("hello, world!", greetingService.hello());
    }

    @Test
    public void should_refused_when_access_greeting_interface() {
        when(greetingService.hello()).thenReturn(null);
        assertEquals(null, greetingService.hello());
    }
}
