package com.fakelsmile.quote.service.controllers;

import com.fakelsmile.quote.service.errors.QuoteNotFoundException;
import com.fakelsmile.quote.service.mappers.QuoteMapper;
import com.fakelsmile.quote.service.models.dto.*;
import com.fakelsmile.quote.service.models.dto.quote.QuoteSaveDTO;
import com.fakelsmile.quote.service.models.dto.quote.QuoteUpdateDTO;
import com.fakelsmile.quote.service.models.dto.quote.ResponseQuoteDTO;
import com.fakelsmile.quote.service.models.entity.QuoteEntity;
import com.fakelsmile.quote.service.services.QuoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * Controller for quotes.
 */
@Validated
@RestController
@RequestMapping(path = "/api/v1/quotes")
@RequiredArgsConstructor
public class QuoteController {
    private final QuoteService service;
    private final QuoteMapper mapper;

    /**
     * Get all the quotes.
     *
     * @return list of quotes
     */
    @GetMapping
    public List<ResponseQuoteDTO> getAllQuotes() {
        List<QuoteEntity> quoteEntityList = service.getAllQuotes();
        return mapper.toListDto(quoteEntityList);
    }

    /**
     * Create a new quote.
     *
     * @param quoteSaveDTO - quote for create
     * @return created quote
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseQuoteDTO createQuote(@Valid @RequestBody QuoteSaveDTO quoteSaveDTO) {
        QuoteEntity quoteEntity = mapper.toEntity(quoteSaveDTO);
        quoteEntity = service.saveQuote(quoteEntity);
        return mapper.toDto(quoteEntity);
    }

    /**
     * Get a quote by id.
     *
     * @param id - quote id
     * @return found quote
     * @throws QuoteNotFoundException if the quote is not found
     */
    @GetMapping("/{id}")
    public ResponseQuoteDTO getQuote(@PathVariable Long id) throws QuoteNotFoundException {
        QuoteEntity quoteEntityId = service.getQuote(id);
        return mapper.toDto(quoteEntityId);
    }

    /**
     * Update a quote by id.
     *
     * @param id             - quote id
     * @param quoteUpdateDTO - quote for update
     * @return updated quote
     * @throws QuoteNotFoundException if the quote is not found
     */
    @PutMapping("/{id}")
    public ResponseQuoteDTO updateQuote(@Valid @RequestBody QuoteUpdateDTO quoteUpdateDTO, @PathVariable Long id) throws QuoteNotFoundException {
        QuoteEntity quoteEntity = mapper.toEntity(quoteUpdateDTO);
        quoteEntity = service.updateQuote(id, quoteEntity);
        return mapper.toDto(quoteEntity);
    }

    /**
     * Delete a quote by id.
     *
     * @param id - quote id
     */
    @DeleteMapping("/{id}")
    public void deleteQuote(@PathVariable Long id) throws QuoteNotFoundException {
        service.deleteQuote(id);
    }

    /**
     * Get a random quote.
     *
     * @return random quote or null if no quote exist
     */
    @GetMapping("/random")
    public ResponseQuoteDTO getRandomQuote() throws QuoteNotFoundException {
        return service.getRandomQuote().map(mapper::toDto).orElse(null);
    }

    /**
     * Get sorted quotes by score with limit.
     *
     * @param directionType - direction type
     * @param limit         - limit
     * @return list of quotes
     */
    @GetMapping("/sort")
    public List<ResponseQuoteDTO> getQuotesOrderByScore(@RequestParam DirectionType directionType, @Min(1) @RequestParam Integer limit) {
        List<QuoteEntity> quotes = service.getQuotesOrderByScore(directionType, limit);
        return mapper.toListDto(quotes);
    }
}
