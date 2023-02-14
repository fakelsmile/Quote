package com.fakelsmile.quote.service.errors;

/**
 * Base exception for not found error
 */
public abstract class NotFoundException extends Exception {
    protected NotFoundException(String string, long id) {
        super(String.format(string, id));
    }
}
