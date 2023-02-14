package com.fakelsmile.quote.service.services;

import com.fakelsmile.quote.service.errors.QuoteNotFoundException;
import com.fakelsmile.quote.service.models.entity.QuoteEntity;
import com.fakelsmile.quote.service.models.entity.VoteType;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuoteScoreServiceTest {
    @InjectMocks
    private QuoteScoreService quoteScoreService;
    @Mock
    private QuoteService quoteService;
    private QuoteEntity quote;
    private QuoteEntity savedQuote;

    @BeforeEach
    public void setup() {
        quote = QuoteEntity.builder()
                .id(1L)
                .content("Ramesh")
                .score(2L)
                .build();

        savedQuote = QuoteEntity.builder()
                .id(1L)
                .content("Ramesh")
                .score(2L)
                .build();
    }

    @AfterEach
    public void turnDown() {
        verifyNoMoreInteractions(quoteService);
    }

    @DisplayName("JUnit test for calculateScore method VoteType = UP, isUpdate = true ")
    @Test
    public void calculateScoreVoteTypeUpIsUpdateTrueTest() throws QuoteNotFoundException {
        boolean isUpdate = true;

        QuoteEntity quoteAfterCalculateScore = QuoteEntity.builder()
                .id(quote.getId())
                .content(quote.getContent())
                .score(quote.getScore() + 2)
                .build();

        when(quoteService.getQuote(quote.getId())).thenReturn(quote);
        when(quoteService.saveQuote(quoteAfterCalculateScore)).thenReturn(savedQuote);

        quoteScoreService.calculateScore(quote.getId(), VoteType.UP, isUpdate);

        verify(quoteService).getQuote(quote.getId());
        verify(quoteService).saveQuote(quoteAfterCalculateScore);
    }


    @DisplayName("JUnit test for calculateScore method VoteType = DOWN, isUpdate = true ")
    @Test
    public void calculateScoreVoteTypeDownIsUpdateTrueTest() throws QuoteNotFoundException {
        boolean isUpdate = true;

        QuoteEntity quoteAfterCalculateScore = QuoteEntity.builder()
                .id(quote.getId())
                .content(quote.getContent())
                .score(quote.getScore() - 2)
                .build();

        when(quoteService.getQuote(quote.getId())).thenReturn(quote);
        when(quoteService.saveQuote(quoteAfterCalculateScore)).thenReturn(savedQuote);

        quoteScoreService.calculateScore(quote.getId(), VoteType.DOWN, isUpdate);

        verify(quoteService).getQuote(quote.getId());
        verify(quoteService).saveQuote(quoteAfterCalculateScore);
    }

    @DisplayName("JUnit test for calculateScore method VoteType = UP, isUpdate = false ")
    @Test
    public void calculateScoreVoteTypeUpIsUpdateFalseTest() throws QuoteNotFoundException {
        boolean isUpdate = false;

        QuoteEntity quoteAfterCalculateScore = QuoteEntity.builder()
                .id(quote.getId())
                .content(quote.getContent())
                .score(quote.getScore() + 1)
                .build();

        when(quoteService.getQuote(quote.getId())).thenReturn(quote);
        when(quoteService.saveQuote(quoteAfterCalculateScore)).thenReturn(savedQuote);

        quoteScoreService.calculateScore(quote.getId(), VoteType.UP, isUpdate);

        verify(quoteService).getQuote(quote.getId());
        verify(quoteService).saveQuote(quoteAfterCalculateScore);
    }

    @DisplayName("JUnit test for calculateScore method VoteType = DOWN, isUpdate = false ")
    @Test
    public void calculateScoreVoteTypeDownIsUpdateFalseTest() throws QuoteNotFoundException {
        boolean isUpdate = false;

        QuoteEntity quoteAfterCalculateScore = QuoteEntity.builder()
                .id(quote.getId())
                .content(quote.getContent())
                .score(quote.getScore() - 1)
                .build();

        when(quoteService.getQuote(quote.getId())).thenReturn(quote);
        when(quoteService.saveQuote(quoteAfterCalculateScore)).thenReturn(savedQuote);

        quoteScoreService.calculateScore(quote.getId(), VoteType.DOWN, isUpdate);

        verify(quoteService).getQuote(quote.getId());
        verify(quoteService).saveQuote(quoteAfterCalculateScore);
    }
}
