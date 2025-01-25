package com.ing.cmc.common.exception;

public class UnauthorizedException extends BaseException {
    public UnauthorizedException(String message, Object[] args) {
        super(message, args);
    }
}
