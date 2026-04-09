package com.breno.marketplace_test.exceptions;

public class InvalidCredentialsException extends GlobalException {
    public InvalidCredentialsException() {
        super("Invalid email or password");
    }
}