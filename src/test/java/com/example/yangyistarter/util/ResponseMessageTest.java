package com.example.yangyistarter.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ResponseMessageTest {

    @Test
    public void should_return_responseMessage_when_call_constructor() {

        ResponseMessage responseMessage = new ResponseMessage(10000, "invalid request");
        Assertions.assertEquals("invalid request", responseMessage.getMessage());

    }

    @Test
    public void should_save_details_when_given_details() {

        //对getDetails()做测试时，只需要考虑出参和入参，不需要考虑内部的实现。
        ResponseMessage responseMessage = new ResponseMessage(10000, "invalid request");
        Assertions.assertEquals(1, responseMessage.withDetails("key", responseMessage).getDetails().size());

    }
}
