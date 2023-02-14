package com.fakelsmile.quote.service.repositories;

import com.fakelsmile.quote.service.models.entity.VoteEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for VoteEntity.
 */
@Repository
public interface VoteRepository extends CrudRepository<VoteEntity, Long> {

    /**
     * Find a vote by the user ID and quote ID if the vote is active.
     *
     * @param userId  the ID of the user.
     * @param quoteId the ID of the quote.
     * @return an {@link Optional} containing the vote, or an empty {@link Optional} if no such vote exists
     */
    @Query("SELECT r FROM VoteEntity r WHERE r.user.id = :userId and r.quote.id = :quoteId and r.isActive = true")
    Optional<VoteEntity> findByUserAndQuoteIsActive(@Param("userId") long userId, @Param("quoteId") long quoteId);

    /**
     * Find all votes by the quote ID
     *
     * @param quoteId the ID of the quote.
     * @return all votes found by the quote ID
     */
    @Query("SELECT r FROM VoteEntity r WHERE r.quote.id = :quoteId")
    List<VoteEntity> findAllByQuote(@Param("quoteId") Long quoteId);

    /**
     * Find all active votes by the quote ID
     *
     * @param quoteId the ID of the quote.
     * @return all active votes found by the quote ID
     */
    @Query("SELECT r FROM VoteEntity r WHERE r.quote.id = :quoteId and r.isActive = true")
    List<VoteEntity> findAllActiveByQuote(@Param("quoteId") Long quoteId);
}
