package com.example.yangyistarter.util;

import lombok.*;

import java.util.Map;

@Getter
@AllArgsConstructor
public class ResponseMessage {
    private int code;
    private String message;
    private Map<String, Object> details;

    /*public <T> ResponseMessage withDetails(String key, T value) {
        details.put(key, value);
        return this;
    }*/
}
