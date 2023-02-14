package com.fakelsmile.quote.service.models.dto.user;

import lombok.Data;

/**
 * Short DTO for user.
 */
@Data
public class UserShortDTO {

    /**
     * The ID of the user.
     */
    private Long id;

    /**
     * The name of the user.
     */
    private String name;
}