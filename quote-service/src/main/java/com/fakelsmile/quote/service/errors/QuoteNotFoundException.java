package com.fakelsmile.quote.service.errors;

/**
 * Exception when quote is not found
 */
public class QuoteNotFoundException extends NotFoundException {
    public QuoteNotFoundException(long id) {
        super("Quote not found by id: %d", id);
    }
}