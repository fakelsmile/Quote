package com.fakelsmile.quote.service.models.dto.vote;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.Instant;

/**
 * Vote for history
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class HistoryVoteDTO extends ResponseVoteDTO {

    /**
     * The creation time of the vote.
     */
    private Instant createAt;

    /**
     * The update time of the vote.
     */
    private Instant updateAt;
}