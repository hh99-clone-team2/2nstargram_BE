package com.sparta.hh2stagram.global.handler.exception;

import lombok.Getter;

@Getter
public class CustomApiException extends RuntimeException {

    public CustomApiException(ErrorCode message) {
        super(String.valueOf(message));
    }
}
