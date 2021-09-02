package com.example.yangyistarter.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RequestTest {
    @Test
    public void should_return_new_string_when_provide_parameter() {
        //{"name": "po", "password": "0" }
        Request request = Request.builder().name("po").password("0").build();
        Assertions.assertEquals("{\"name\": \"po\", \"password\": \"0\"}", request.toString());
    }
}
