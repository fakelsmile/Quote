package com.fakelsmile.quote.service.services;

import com.fakelsmile.quote.service.errors.UserAlreadyExistException;
import com.fakelsmile.quote.service.errors.UserNotFoundException;
import com.fakelsmile.quote.service.models.entity.UserEntity;
import com.fakelsmile.quote.service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for users.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    /**
     * Save a new user.
     *
     * @param user - user
     * @return saved user
     * @throws UserAlreadyExistException if the user is already exist with name or email
     */
    public UserEntity saveUser(UserEntity user) throws UserAlreadyExistException {
        Optional<UserEntity> optUser = userRepository.findByNameOrEmail(user.getName(), user.getEmail());
        if (optUser.isPresent()) {
            throw new UserAlreadyExistException(user.getName(), user.getEmail());
        }
        user = userRepository.save(user);
        return user;
    }

    /**
     * Get a user by id.
     *
     * @param id - user id
     * @return found user
     * @throws UserNotFoundException if the user is not found
     */
    public UserEntity getUser(long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

}
