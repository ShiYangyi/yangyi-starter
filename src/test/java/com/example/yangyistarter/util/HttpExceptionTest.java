package com.example.yangyistarter.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HttpExceptionTest {

    @Test
    public void should_return_responseCode_when_call_get_method() {

        ResponseCode responseCode = ResponseCode.INVALID_REQUEST;
        HttpException httpException = new HttpException(responseCode, "message");
        Assertions.assertEquals(responseCode, httpException.getResponseCode());

    }

    @Test
    public void should_return_httpException_when_call_constructor() {

        ResponseCode responseCode = ResponseCode.INVALID_REQUEST;
        HttpException httpException = new HttpException(responseCode, "message");
        Assertions.assertEquals("message", httpException.getMessage());

    }
}
