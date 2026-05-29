package com.breno.marketplace_test.exceptions;

public class EmailAlreadyExistsException extends GlobalException {
    public EmailAlreadyExistsException(String email) {
        super("Email already exists: " + email);
    }
}
