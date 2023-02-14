package com.fakelsmile.quote.service.services;

import com.fakelsmile.quote.service.errors.QuoteNotFoundException;
import com.fakelsmile.quote.service.models.dto.DirectionType;
import com.fakelsmile.quote.service.models.entity.QuoteEntity;
import com.fakelsmile.quote.service.repositories.QuoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Service for quotes.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QuoteService {

    private final QuoteRepository quoteRepository;

    private static final Random RANDOM = new Random();

    /**
     * Get all the quotes.
     *
     * @return list of quotes
     */
    public List<QuoteEntity> getAllQuotes() {
        return (List<QuoteEntity>) quoteRepository.findAll();
    }

    /**
     * Get sorted quotes by score with limit.
     *
     * @param directionType - direction type
     * @param limit         - limit
     * @return list of quotes
     */
    public List<QuoteEntity> getQuotesOrderByScore(DirectionType directionType, Integer limit) {
        Sort.Direction sort = switch (directionType) {
            case ASC -> Sort.Direction.ASC;
            case DESC -> Sort.Direction.DESC;
        };
        return quoteRepository.findAll(PageRequest.of(0, limit, sort, "score")).toList();
    }

    /**
     * Save a new quote.
     *
     * @param quote - quote
     * @return saved quote
     */
    public QuoteEntity saveQuote(QuoteEntity quote) {
        quote = quoteRepository.save(quote);
        return quote;
    }

    /**
     * Get a quote by id.
     *
     * @param id - quote id
     * @return found quote
     * @throws QuoteNotFoundException if the quote is not found
     */
    public QuoteEntity getQuote(long id) throws QuoteNotFoundException {
        return quoteRepository.findById(id).orElseThrow(() -> new QuoteNotFoundException(id));
    }

    /**
     * Get random quote.
     *
     * @return an {@link Optional} containing the random quote, or an empty {@link Optional} if no quote exists
     */
    public Optional<QuoteEntity> getRandomQuote() throws QuoteNotFoundException {
        long quotesCount = quoteRepository.count();
        if (quotesCount == 0) {
            return Optional.empty();
        }
        long randomQuoteId = RANDOM.nextLong(1, quotesCount + 1);
        return Optional.of(getQuote(randomQuoteId));
    }

    /**
     * Update a quote by id.
     *
     * @param id    - quote id
     * @param quote quote
     * @return updated quote
     * @throws QuoteNotFoundException if the quote is not found
     */
    public QuoteEntity updateQuote(long id, QuoteEntity quote) throws QuoteNotFoundException {
        QuoteEntity foundQuote = quoteRepository.findById(id).orElseThrow(() -> new QuoteNotFoundException(id));
        foundQuote.setContent(quote.getContent());
        foundQuote = saveQuote(foundQuote);
        return foundQuote;
    }

    /**
     * Delete a quote by id.
     *
     * @param id - quote id
     */
    public void deleteQuote(long id) throws QuoteNotFoundException {
        QuoteEntity quoteEntity = getQuote(id);
        quoteRepository.delete(quoteEntity);
    }
}

