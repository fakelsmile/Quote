package com.fakelsmile.quote.service.errors;

/**
 * Exception when vote already exist
 */
public class UserAlreadyExistException extends AlreadyExistException {
    public UserAlreadyExistException(String userName, String userEmail) {
        super("User already exist by name: " + userName + " or email: " + userEmail);
    }
}
