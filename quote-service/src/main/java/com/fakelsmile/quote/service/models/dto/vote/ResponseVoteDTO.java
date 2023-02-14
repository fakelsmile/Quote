package com.fakelsmile.quote.service.models.dto.vote;

import com.fakelsmile.quote.service.models.dto.user.UserShortDTO;
import com.fakelsmile.quote.service.models.entity.VoteType;
import lombok.Data;

/**
 * Base DTO for vote.
 */
@Data
public class ResponseVoteDTO {

    /**
     * The ID of the vote.
     */
    private Long id;

    /**
     * The user who cast the vote.
     */
    private UserShortDTO user;

    /**
     * The type of the vote, either UP or DOWN.
     */
    private VoteType voteType;
}
