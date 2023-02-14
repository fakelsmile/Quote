package com.fakelsmile.quote.service.models.dto.vote;

import com.fakelsmile.quote.service.models.entity.VoteType;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Base DTO for vote.
 */
@Data
public class RequestVoteDTO {

    /**
     * The ID of the vote.
     */
    private Long id;

    /**
     * The ID of the user who cast the vote.
     */
    @NotNull(message = "User Id cannot be empty")
    private Long userId;

    /**
     * The ID of the quote that the vote is cast on.
     */
    @NotNull(message = "Quote Id cannot be empty")
    private Long quoteId;

    /**
     * The type of the vote.
     */
    private VoteType voteType;
}
