package com.example.yangyistarter.util;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.ImmutableSet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public enum ResponseCode {
    //error code
    USER_ALREADY_EXISTS(10000, "user already exists"),
    USER_REGISTER_SUCCESS(10001, "user registered successful");

    private static final Set<String> Response_CODE_VALUES = ImmutableSet.copyOf(
            Arrays.stream(values()).map(Enum::toString).collect(Collectors.toSet())
    );

    private final int code;
    private final String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @JsonValue
    public ResponseMessage toResponseMessage() {
        return new ResponseMessage(code, message, new HashMap<>());
    }
}
