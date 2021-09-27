package com.example.yangyistarter.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ResponseCodeTest {

    @Test
    public void should_return_code_1000_when_call_getCode() {
        ResponseCode responseCode = ResponseCode.INVALID_REQUEST;
        Assertions.assertEquals(10000, responseCode.getCode());
    }

    @Test
    public void should_return_true_when_contain_code_10001() {
        Assertions.assertTrue(ResponseCode.contains("10001"));
    }

    @Test
    public void should_return_false_given_code_10006() {
        Assertions.assertFalse(ResponseCode.contains("11006"));
    }
}
