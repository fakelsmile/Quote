package com.fakelsmile.quote.service.errors;

/**
 * Exception when vote already exist
 */
public class VoteAlreadyExistException extends AlreadyExistException {
    public VoteAlreadyExistException(long userId, long quoteId) {
        super("Vote already exist by user id: " + userId + " and quote id: " + quoteId);
    }
}
