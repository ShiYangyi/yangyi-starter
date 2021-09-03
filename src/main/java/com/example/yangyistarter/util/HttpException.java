package com.example.yangyistarter.util;

import lombok.Getter;

public class HttpException extends RuntimeException {
    @Getter
    private ResponseCode responseCode;

    public HttpException(ResponseCode code, String message) {
        super(message);
        this.responseCode = code;
    }

    public HttpException(ResponseCode code, Throwable cause) {
        super(code.getMessage(), cause);
        this.responseCode = code;
    }

    public HttpException(ResponseCode code, String message, Throwable cause) {
        super(message, cause);
        this.responseCode = code;
    }

    public HttpException(ResponseCode code) {
        this(code, code.getMessage());
    }
}
