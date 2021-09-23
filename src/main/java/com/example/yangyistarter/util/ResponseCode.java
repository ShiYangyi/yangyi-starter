package com.example.yangyistarter.util;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.ImmutableSet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public enum ResponseCode {
    INVALID_REQUEST(10000, "invalid request"),
    SERVER_ERROR(10001, "server error"),
    USER_ALREADY_EXISTS(10002, "user already exists"),
    USER_REGISTER_SUCCESS(10003, "user registered successful"),
    INVALID_USER_INFO(10004, "invalid user information"),
    PARKINGLOT_ADD_SUCCESS(10005, "parking lot add success"),
    ROLE_IS_NOT_MANAGER(10006, "role is not manager"),
    PARKINGLOT_ALREADY_EXISTS(10007, "parking lot already exists"),
    PARKINGLOT_DELETE_SUCCESS(10008, "parking lot delete success"),
    PARKINGLOT_NOT_EXIST(10009, "parking lot not exist"),
    USER_DELETE_SUCCESS(10010,"user delete successful"),
    USER_NOT_EXIST(10011, "user not exist"),
    RECEIPT_INVALID(10012, "receipt is invalid"),
    PICK_UP_CAR_SUCCESSFUL(10013, "pick up car successful"),
    PARKING_SPACE_INVALID(10014, "free parking space and invalid request");

    private static final Set<String> Response_CODE_VALUES = ImmutableSet.copyOf(
            //注意写法，把获取到的code组成set集合。
            Arrays.stream(values()).map((item)->String.valueOf(item.getCode())).collect(Collectors.toSet())
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
    public int getCode() {
        return code;
    }

    @JsonValue
    public ResponseMessage toResponseMessage() {
        return new ResponseMessage(code, message, new HashMap<>());
    }

    public static boolean contains(String code) {
        return Response_CODE_VALUES.contains(code);
    }
}
