package com.fakelsmile.quote.service.models.dto.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * Base DTO for user.
 */
@Data
public class RequestUserDTO {

    /**
     * The name of the user.
     */
    @NotBlank(message = "Name cannot be empty")
    private String name;

    /**
     * The email of the user.
     */
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    /**
     * The password of the user.
     */
    @NotBlank(message = "Password cannot be empty")
    private String password;
}
