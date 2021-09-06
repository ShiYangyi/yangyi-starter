package com.example.yangyistarter.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolationException;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class GlobalExceptionHandlerTest {

    @Autowired
    GlobalExceptionHandler globalExceptionHandler;

    @Test
    public void should_return_constraint_violation_exception_when_Violate_constraint() {

        ConstraintViolationException ex = new ConstraintViolationException("message", null);
        Assertions.assertEquals(ResponseCode.INVALID_REQUEST.getCode(), globalExceptionHandler.handleConstraintViolationException(ex).getCode());

    }

    /*@Test
    public void should_return_response_when_error_size_is_1() {

        MethodArgumentNotValidException methodArgumentNotValidException = mock(MethodArgumentNotValidException.class);
        ObjectError objectError = new ObjectError("objectName", "invalid request");
        BindingResult bindingResult = mock(BindingResult.class);

        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(objectError));
        when(ResponseCode.valueOf(ResponseCode.class, "SERVER_ERROR")).thenReturn(ResponseCode.SERVER_ERROR);

        Assertions.assertEquals("server error", globalExceptionHandler.handleHttpClientError(methodArgumentNotValidException).getMessage());
    }*/

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
