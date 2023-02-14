package com.fakelsmile.quote.service.services;

import com.fakelsmile.quote.service.errors.QuoteNotFoundException;
import com.fakelsmile.quote.service.models.entity.QuoteEntity;
import com.fakelsmile.quote.service.models.entity.VoteType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service for calculate score for quote.
 */
@Service
@RequiredArgsConstructor
public class QuoteScoreService {
    private final QuoteService quoteService;

    /**
     * Calculate score based on Vote Type.
     *
     * @param quoteId  - quote id
     * @param voteType - type of vote
     * @param isUpdate - is calculating when update vote
     */
    public void calculateScore(Long quoteId, VoteType voteType, boolean isUpdate) throws QuoteNotFoundException {
        QuoteEntity quoteEntity = quoteService.getQuote(quoteId);
        //when updating, we need to change value on 2 points, when new vote - change 1 point
        int point = isUpdate ? 2 : 1;
        quoteEntity.setScore(switch (voteType) {
            case UP -> quoteEntity.getScore() + point;
            case DOWN -> quoteEntity.getScore() - point;
        });
        quoteService.saveQuote(quoteEntity);
    }
}