package com.breno.marketplace_test.exceptions;

public class InvalidTokenException extends GlobalException {
    public InvalidTokenException(String message) {
        super("Invalid token: " + message);
    }
}