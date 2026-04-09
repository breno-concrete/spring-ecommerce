package com.breno.marketplace_test.exceptions;

public class UserAlreadyExistsException extends GlobalException {
    public UserAlreadyExistsException(String email) {
        super("User with email " + email + " already exists");
    }
}