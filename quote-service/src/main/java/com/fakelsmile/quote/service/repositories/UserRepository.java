package com.fakelsmile.quote.service.repositories;

import com.fakelsmile.quote.service.models.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for UserEntity.
 */
@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    /**
     * Finds a user with the given name or email.
     *
     * @param name  the name of the user to find
     * @param email the email of the user to find
     * @return an {@link Optional} containing the user, or an empty {@link Optional} if no such user exists
     */
    Optional<UserEntity> findByNameOrEmail(String name, String email);
}