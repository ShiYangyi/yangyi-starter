package com.example.yangyistarter.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class GlobalExceptionHandlerTest {

    @Autowired
    GlobalExceptionHandler globalExceptionHandler;

    @Test
    public void should_return_response_when_encounter_methodArgumentNotValidException() {
        ArrayList<ObjectError> list = new ArrayList<>();
        ObjectError objectError = new ObjectError("objectName", "invalid request");
        list.add(objectError);
        list.add(objectError);

        MethodArgumentNotValidException methodArgumentNotValidException = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(list);

        Assertions.assertEquals("invalid request", globalExceptionHandler.handleHttpClientError(methodArgumentNotValidException).getMessage());
    }

}
