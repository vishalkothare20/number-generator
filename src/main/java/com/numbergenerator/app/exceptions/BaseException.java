package com.numbergenerator.app.exceptions;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final String message;
    private final Integer code;

    public BaseException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }


    public BaseException(String message) {
        this.message = message;
        this.code = 400;
    }
}
