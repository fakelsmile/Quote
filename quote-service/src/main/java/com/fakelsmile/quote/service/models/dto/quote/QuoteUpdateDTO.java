package com.fakelsmile.quote.service.models.dto.quote;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * DTO for update quote.
 */
@Data
public class QuoteUpdateDTO {
    /**
     * The content of the quote
     */
    @NotBlank(message = "Content cannot be empty")
    private String content;
}
