package com.fakelsmile.quote.service.errors;

/**
 * Exception when vote is not found
 */
public class VoteNotFoundException extends NotFoundException {
    public VoteNotFoundException(long id) {
        super("Vote not found by id: %d", id);
    }
}
