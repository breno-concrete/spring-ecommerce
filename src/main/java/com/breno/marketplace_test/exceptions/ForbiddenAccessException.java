package com.breno.marketplace_test.exceptions;

public class ForbiddenAccessException extends GlobalException {
    public ForbiddenAccessException(String message) {
        super(message);
    }
}
