package com.fakelsmile.quote.service.services;

import com.fakelsmile.quote.service.errors.QuoteNotFoundException;
import com.fakelsmile.quote.service.models.dto.DirectionType;
import com.fakelsmile.quote.service.models.entity.QuoteEntity;
import com.fakelsmile.quote.service.repositories.QuoteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class QuoteServiceTest {

    @InjectMocks
    private QuoteService quoteService;
    @Mock
    private QuoteRepository quoteRepository;
    private QuoteEntity quote;
    private QuoteEntity savedQuote;

    @BeforeEach
    public void setup() {
        quote = QuoteEntity.builder()
                .content("Ramesh")
                .build();

        savedQuote = QuoteEntity.builder()
                .id(1L)
                .content("Ramesh")
                .build();
    }

    @AfterEach
    public void turnDown() {
        verifyNoMoreInteractions(quoteRepository);
    }

    @DisplayName("JUnit test for saveQuote method")
    @Test
    public void saveQuoteTest() {
        when(quoteRepository.save(quote)).thenReturn(savedQuote);
        QuoteEntity actual = quoteService.saveQuote(quote);

        assertEquals(savedQuote, actual);
        verify(quoteRepository).save(quote);
    }

    @DisplayName("JUnit test for getAllQuotes method")
    @Test
    public void getAllQuotesTest() {
        when(quoteRepository.findAll()).thenReturn(List.of(quote, savedQuote));
        List<QuoteEntity> quotes = quoteService.getAllQuotes();

        assertNotNull(quotes);
        assertEquals(2, quotes.size());
        assertEquals(quote, quotes.get(0));
        assertEquals(savedQuote, quotes.get(1));
        verify(quoteRepository).findAll();
    }

    @DisplayName("JUnit test for getQuote method")
    @Test
    public void getQuoteTest() throws QuoteNotFoundException {
        when(quoteRepository.findById(1L)).thenReturn(Optional.of(savedQuote));
        QuoteEntity actual = quoteService.getQuote(savedQuote.getId());

        assertEquals(savedQuote, actual);
        verify(quoteRepository).findById(1L);
    }

    @DisplayName("JUnit test for updateQuote method")
    @Test
    public void updateQuoteTest() throws QuoteNotFoundException {
        QuoteEntity quoteForUpdate = QuoteEntity.builder()
                .id(1L)
                .content("Rh")
                .build();

        when(quoteRepository.findById(1L)).thenReturn(Optional.of(quoteForUpdate));
        when(quoteRepository.save(savedQuote)).thenReturn(savedQuote);

        QuoteEntity actual = quoteService.updateQuote(savedQuote.getId(), savedQuote);

        assertEquals(savedQuote, actual);
        verify(quoteRepository).findById(1L);
        verify(quoteRepository).save(savedQuote);
    }

    @DisplayName("JUnit test for deleteQuote method")
    @Test
    public void deleteQuoteTest() throws QuoteNotFoundException {
        long quoteId = 1L;
        when(quoteRepository.findById(quoteId)).thenReturn(Optional.of(quote));
        quoteService.deleteQuote(quoteId);

        verify(quoteRepository).findById(1L);
        verify(quoteRepository).delete(quote);
    }

    @DisplayName("JUnit test for updateQuote method with error")
    @Test
    public void updateQuoteWithErrorTest() {
        when(quoteRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(QuoteNotFoundException.class, () -> quoteService.updateQuote(savedQuote.getId(), savedQuote));

        verify(quoteRepository).findById(1L);
    }

    @DisplayName("JUnit test for getQuote method with error")
    @Test
    public void getQuoteWithErrorTest() {
        when(quoteRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(QuoteNotFoundException.class, () -> quoteService.getQuote(savedQuote.getId()));

        verify(quoteRepository).findById(1L);
    }

    @DisplayName("JUnit test for getQuotesOrderByScore method, direction = ASC")
    @Test
    public void getQuotesOrderByScoreAscTest() {
        List<QuoteEntity> expected = List.of(quote);
        Page<QuoteEntity> page = new PageImpl<>(expected);
        int limit = 10;
        when(quoteRepository.findAll(PageRequest.of(0, limit, Sort.Direction.ASC, "score"))).thenReturn(page);
        List<QuoteEntity> actual = quoteService.getQuotesOrderByScore(DirectionType.ASC, limit);

        assertEquals(expected, actual);
        verify(quoteRepository).findAll(PageRequest.of(0, limit, Sort.Direction.ASC, "score"));
    }

    @DisplayName("JUnit test for getQuotesOrderByScore method, direction = DESC")
    @Test
    public void getQuotesOrderByScoreDescTest() {
        List<QuoteEntity> expected = List.of(quote);
        Page<QuoteEntity> page = new PageImpl<>(expected);
        int limit = 10;
        when(quoteRepository.findAll(PageRequest.of(0, limit, Sort.Direction.DESC, "score"))).thenReturn(page);
        List<QuoteEntity> actual = quoteService.getQuotesOrderByScore(DirectionType.DESC, limit);

        assertEquals(expected, actual);
        verify(quoteRepository).findAll(PageRequest.of(0, limit, Sort.Direction.DESC, "score"));
    }
}
