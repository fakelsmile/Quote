package com.fakelsmile.quote.service.models.dto.quote;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * DTO for save quote.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class QuoteSaveDTO extends QuoteUpdateDTO {

    /**
     * The ID of the author of the quote
     */
    private Long authorId;
}
