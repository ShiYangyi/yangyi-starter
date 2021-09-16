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

        Assertions.assertEquals(true, ResponseCode.contains("10001"));
    }

    @Test
    public void should_return_false_given_code_10006() {

        //final Set<String> Response_CODE_VALUES = Stream.of("10000").collect(Collectors.toSet());
        //测试ResponseCode中的contains()，只需要考虑该方法的入参和出参，不需要考虑该方法内部的实现。所以不需要对Response_CODE_VALUES赋值。
        Assertions.assertEquals(false, ResponseCode.contains("11006"));
    }
}
