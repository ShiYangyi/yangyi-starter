package com.example.yangyistarter.util;

public class HttpException extends RuntimeException {
    private ResponseCode responseCode;

    public HttpException(ResponseCode code, String message) {
        super(message);
        this.responseCode = code;
    }

    public ResponseCode getResponseCode() {
        return this.responseCode;
    }

    /*public HttpException(ResponseCode code, Throwable cause) {
        super(code.getMessage(), cause);
        this.responseCode = code;
    }

    public HttpException(ResponseCode code, String message, Throwable cause) {
        super(message, cause);
        this.responseCode = code;
    }

    public HttpException(ResponseCode code) {
        this(code, code.getMessage());
    }*/
}
