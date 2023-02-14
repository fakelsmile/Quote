package com.fakelsmile.quote.service.models.dto.quote;

import com.fakelsmile.quote.service.models.dto.user.UserShortDTO;
import lombok.Data;

/**
 * Base DTO for quote.
 */
@Data
public class ResponseQuoteDTO {

    /**
     * The ID of the quote.
     */
    private Long id;

    /**
     * The content of the quote.
     */
    private String content;

    /**
     * The score of the quote.
     */
    private Long score;

    /**
     * The author of the quote represented as a UserShortDTO object.
     */
    private UserShortDTO author;
}
