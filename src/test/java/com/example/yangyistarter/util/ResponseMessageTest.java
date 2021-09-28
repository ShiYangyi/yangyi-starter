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

        ResponseMessage responseMessage = new ResponseMessage(10000, "invalid request");
        Assertions.assertEquals(1, responseMessage.withDetails("key", responseMessage).getDetails().size());

    }
}
