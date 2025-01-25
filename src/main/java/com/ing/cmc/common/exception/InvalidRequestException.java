package com.ing.cmc.common.exception;

public class InvalidRequestException extends BaseException {
    public InvalidRequestException(String message, Object[] args) {
        super(message, args);
    }
}