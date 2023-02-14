package com.fakelsmile.quote.service.services;

import com.fakelsmile.quote.service.errors.NotFoundException;
import com.fakelsmile.quote.service.errors.QuoteNotFoundException;
import com.fakelsmile.quote.service.errors.VoteAlreadyExistException;
import com.fakelsmile.quote.service.errors.VoteNotFoundException;
import com.fakelsmile.quote.service.models.entity.VoteEntity;
import com.fakelsmile.quote.service.models.entity.VoteType;
import com.fakelsmile.quote.service.repositories.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service for votes.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final QuoteScoreService quoteScoreService;

    /**
     * Save a new vote.
     *
     * @param vote - vote
     * @return saved vote
     * @throws VoteAlreadyExistException if the vote already exist
     * @throws QuoteNotFoundException    if the quote is not found
     */
    @Transactional
    public VoteEntity saveVote(VoteEntity vote) throws VoteAlreadyExistException, QuoteNotFoundException {
        Optional<VoteEntity> optVote = voteRepository.findByUserAndQuoteIsActive(vote.getUser().getId(), vote.getQuote().getId());
        if (optVote.isPresent()) {
            throw new VoteAlreadyExistException(vote.getUser().getId(), vote.getQuote().getId());
        }
        vote = voteRepository.save(vote);
        quoteScoreService.calculateScore(vote.getQuote().getId(), vote.getVoteType(), false);
        return vote;
    }

    /**
     * Get a vote by id.
     *
     * @param id - vote id
     * @return found vote
     * @throws VoteNotFoundException if the vote is not found
     */
    public VoteEntity getVote(long id) throws VoteNotFoundException {
        return voteRepository.findById(id).orElseThrow(() -> new VoteNotFoundException(id));
    }

    /**
     * Update a vote by id.
     *
     * @param id       - vote id
     * @param voteType - type of vote
     * @return updated vote
     * @throws NotFoundException if the vote/quote is not found
     */
    @Transactional
    public VoteEntity updateVote(long id, VoteType voteType) throws NotFoundException {
        VoteEntity foundVote = voteRepository.findById(id).orElseThrow(() -> new VoteNotFoundException(id));
        if (!foundVote.getVoteType().equals(voteType)) {
            foundVote.setIsActive(false);
            VoteEntity vote = VoteEntity.builder()
                    .user(foundVote.getUser())
                    .quote(foundVote.getQuote())
                    .voteType(voteType)
                    .isActive(true)
                    .build();
            voteRepository.saveAll(List.of(foundVote, vote));
            quoteScoreService.calculateScore(vote.getQuote().getId(), voteType, true);
            return vote;
        }
        return foundVote;
    }

    /**
     * Change a vote status by id.
     *
     * @param id - vote id
     * @throws NotFoundException if the vote/quote is not found
     */
    @Transactional
    public void deleteVote(long id) throws NotFoundException {
        VoteEntity vote = getVote(id);
        if (Boolean.TRUE.equals(vote.getIsActive())) {
            vote.setIsActive(false);
            voteRepository.save(vote);
            switch (vote.getVoteType()) {
                case UP -> quoteScoreService.calculateScore(vote.getQuote().getId(), VoteType.DOWN, false);
                case DOWN -> quoteScoreService.calculateScore(vote.getQuote().getId(), VoteType.UP, false);
            }
        }
    }

    /**
     * Find all votes by the quote ID
     *
     * @param quoteId the ID of the quote.
     * @return all votes found by the quote ID
     */
    public List<VoteEntity> getAllByQuote(Long quoteId) {
        return voteRepository.findAllByQuote(quoteId);
    }

    /**
     * Find all active votes by the quote ID
     *
     * @param quoteId the ID of the quote.
     * @return all votes found by the quote ID
     */
    public List<VoteEntity> getAllActiveByQuote(Long quoteId) {
        return voteRepository.findAllActiveByQuote(quoteId);
    }
}
