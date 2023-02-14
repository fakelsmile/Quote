package com.fakelsmile.quote.service.models.dto;

import lombok.*;

/**
 * Base DTO for error.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDTO {

    /**
     * The name of the field that caused the error.
     */
    private String fieldName;

    /**
     * The error message.
     */
    private String message;
}