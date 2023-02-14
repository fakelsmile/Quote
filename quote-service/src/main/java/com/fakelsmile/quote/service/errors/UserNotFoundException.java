package com.fakelsmile.quote.service.errors;

/**
 * Exception when user is not found
 */
public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(long id) {
        super("User not found by id: %d", id);
    }
}