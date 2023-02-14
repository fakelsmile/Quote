package com.fakelsmile.quote.service.errors;

/**
 * Base exception for already exist error
 */
public abstract class AlreadyExistException extends Exception {
    protected AlreadyExistException(String str) {
        super(str);
    }
}