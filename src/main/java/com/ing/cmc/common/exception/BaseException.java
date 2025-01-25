package com.ing.cmc.common.exception;

import lombok.Getter;

public class BaseException extends Exception {
    private final String message;
    @Getter
    private final Object[] args;

    public BaseException(String message, Object[] args) {
        super(message);
        this.message = message;
        this.args = args;
    }

    @Override
    public String getMessage() {
        return message;
    }
}