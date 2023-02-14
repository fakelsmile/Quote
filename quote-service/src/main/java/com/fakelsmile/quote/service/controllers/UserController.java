package com.fakelsmile.quote.service.controllers;

import com.fakelsmile.quote.service.errors.UserAlreadyExistException;
import com.fakelsmile.quote.service.errors.UserNotFoundException;
import com.fakelsmile.quote.service.mappers.UserMapper;
import com.fakelsmile.quote.service.models.dto.user.RequestUserDTO;
import com.fakelsmile.quote.service.models.dto.user.UserShortDTO;
import com.fakelsmile.quote.service.models.entity.UserEntity;
import com.fakelsmile.quote.service.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Controller for users.
 */
@Validated
@RestController
@RequestMapping(path = "/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;
    private final UserMapper mapper;

    /**
     * Create a new user.
     *
     * @param requestUserDTO - user
     * @return created user
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserShortDTO createUser(@Valid @RequestBody RequestUserDTO requestUserDTO) throws UserAlreadyExistException {
        UserEntity userEntity = mapper.toEntity(requestUserDTO);
        userEntity = service.saveUser(userEntity);
        return mapper.toDto(userEntity);
    }

    /**
     * Get a user by id.
     *
     * @param id - user id
     * @return found user
     * @throws UserNotFoundException if the user is not found
     */
    @GetMapping("/{id}")
    public UserShortDTO getUser(@PathVariable Long id) throws UserNotFoundException {
        UserEntity userEntityId = service.getUser(id);
        return mapper.toDto(userEntityId);
    }

}