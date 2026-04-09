package com.breno.marketplace_test.exceptions;

public class UserNotFoundException extends GlobalException {
    public UserNotFoundException(String email) {
        super("User with email " + email + " not found");
    }

    public UserNotFoundException(Long id) {
        super("User with id " + id + " not found");
    }
}