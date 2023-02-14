package com.fakelsmile.quote.service.services;

import com.fakelsmile.quote.service.errors.NotFoundException;
import com.fakelsmile.quote.service.errors.QuoteNotFoundException;
import com.fakelsmile.quote.service.errors.VoteAlreadyExistException;
import com.fakelsmile.quote.service.errors.VoteNotFoundException;
import com.fakelsmile.quote.service.models.entity.QuoteEntity;
import com.fakelsmile.quote.service.models.entity.UserEntity;
import com.fakelsmile.quote.service.models.entity.VoteEntity;
import com.fakelsmile.quote.service.models.entity.VoteType;
import com.fakelsmile.quote.service.repositories.VoteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class VoteServiceTest {
    @InjectMocks
    private VoteService voteService;
    @Mock
    private QuoteScoreService quoteScoreService;
    private QuoteEntity quote;
    @Mock
    private VoteRepository voteRepository;
    private VoteEntity vote;
    private VoteEntity savedVote;

    @BeforeEach
    public void setup() {
        UserEntity user = UserEntity.builder()
                .id(1L)
                .name("Zaph")
                .email("Zaph@gmail.com")
                .password("qwerty123")
                .build();

        quote = QuoteEntity.builder()
                .id(1L)
                .content("Ramesh")
                .build();

        vote = VoteEntity.builder()
                .id(1L)
                .user(user)
                .quote(quote)
                .voteType(VoteType.UP)
                .isActive(true)
                .build();

        savedVote = VoteEntity.builder()
                .id(1L)
                .user(user)
                .quote(quote)
                .voteType(VoteType.UP)
                .isActive(true)
                .build();
    }

    @AfterEach
    public void turnDown() {
        verifyNoMoreInteractions(voteRepository);
    }

    @DisplayName("JUnit test for saveVote method")
    @Test
    public void saveVoteTest() throws VoteAlreadyExistException, QuoteNotFoundException {
        when(voteRepository.findByUserAndQuoteIsActive(vote.getUser().getId(), vote.getQuote().getId())).thenReturn(Optional.empty());
        when(voteRepository.save(vote)).thenReturn(savedVote);
        VoteEntity actual = voteService.saveVote(vote);

        assertEquals(savedVote, actual);
        verify(voteRepository).save(vote);
        verify(voteRepository).findByUserAndQuoteIsActive(vote.getUser().getId(), vote.getQuote().getId());
    }

    @DisplayName("JUnit test for getVote method")
    @Test
    public void getVoteTest() throws VoteNotFoundException {
        when(voteRepository.findById(1L)).thenReturn(Optional.of(savedVote));
        VoteEntity actual = voteService.getVote(savedVote.getId());

        assertEquals(savedVote, actual);
        verify(voteRepository).findById(1L);
    }

    @DisplayName("JUnit test for updateVote method")
    @Test
    public void updateVoteTest() throws NotFoundException {

        vote.setId(null);
        savedVote.setId(null);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("Zaph");
        userEntity.setEmail("ramesh@gmail.com");
        userEntity.setPassword("Fadatare");

        QuoteEntity quoteEntity = new QuoteEntity();
        quoteEntity.setId(1L);
        quoteEntity.setContent("Ramesh");
        quoteEntity.setAuthor(userEntity);

        VoteEntity foundVote = VoteEntity.builder()
                .id(1L)
                .user(userEntity)
                .quote(quoteEntity)
                .voteType(VoteType.DOWN)
                .isActive(true)
                .build();

        when(voteRepository.findById(foundVote.getId())).thenReturn(Optional.of(foundVote));
        when(voteRepository.saveAll(List.of(foundVote, vote))).thenReturn(List.of(foundVote, vote));

        VoteEntity actual = voteService.updateVote(foundVote.getId(), savedVote.getVoteType());

        assertEquals(savedVote, actual);
        verify(voteRepository).findById(foundVote.getId());
        verify(voteRepository).saveAll(List.of(foundVote, vote));
    }

    @DisplayName("JUnit test for deleteVote method IsActive = true, VoteType = UP")
    @Test
    public void deleteVoteIsActiveTrueVoteTypeUpTest() throws NotFoundException {
        VoteEntity voteAfterChangeIsActive = VoteEntity.builder()
                .id(vote.getId())
                .isActive(false)
                .voteType(vote.getVoteType())
                .quote(vote.getQuote())
                .user(vote.getUser())
                .createAt(vote.getCreateAt())
                .updateAt(vote.getUpdateAt())
                .build();
        when(voteRepository.findById(vote.getId())).thenReturn(Optional.of(vote));
        when(voteRepository.save(voteAfterChangeIsActive)).thenReturn(voteAfterChangeIsActive);
        voteService.deleteVote(savedVote.getId());

        verify(voteRepository).findById(vote.getId());
        verify(voteRepository).save(voteAfterChangeIsActive);
        verify(quoteScoreService).calculateScore(vote.getQuote().getId(), VoteType.DOWN, false);
    }

    @DisplayName("JUnit test for deleteVote method IsActive = true, VoteType = DOWN")
    @Test
    public void deleteVoteIsActiveTrueVoteTypeDownTest() throws NotFoundException {
        vote.setVoteType(VoteType.DOWN);
        VoteEntity voteAfterChangeIsActive = VoteEntity.builder()
                .id(vote.getId())
                .isActive(false)
                .voteType(vote.getVoteType())
                .quote(vote.getQuote())
                .user(vote.getUser())
                .createAt(vote.getCreateAt())
                .updateAt(vote.getUpdateAt())
                .build();
        when(voteRepository.findById(vote.getId())).thenReturn(Optional.of(vote));
        when(voteRepository.save(voteAfterChangeIsActive)).thenReturn(voteAfterChangeIsActive);
        voteService.deleteVote(savedVote.getId());

        verify(voteRepository).findById(vote.getId());
        verify(voteRepository).save(voteAfterChangeIsActive);
        verify(quoteScoreService).calculateScore(vote.getQuote().getId(), VoteType.UP, false);
    }

    @DisplayName("JUnit test for deleteVote method IsActive = false")
    @Test
    public void deleteVoteIsActiveFalseTest() throws NotFoundException {
        long voteId = 1L;
        vote.setIsActive(false);
        when(voteRepository.findById(voteId)).thenReturn(Optional.of(vote));
        voteService.deleteVote(savedVote.getId());

        verify(voteRepository).findById(voteId);
    }

    @DisplayName("JUnit test for deleteVote method with error")
    @Test
    public void deleteVoteWithErrorTest() {
        long voteId = 1L;

        when(voteRepository.findById(voteId)).thenReturn(Optional.empty());
        assertThrows(VoteNotFoundException.class, () -> voteService.deleteVote(savedVote.getId()));

        verify(voteRepository).findById(voteId);
    }

    @DisplayName("JUnit test for updateVote method with error")
    @Test
    public void updateVoteWithErrorTest() {
        when(voteRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(VoteNotFoundException.class, () -> voteService.updateVote(savedVote.getId(), savedVote.getVoteType()));

        verify(voteRepository).findById(1L);
    }

    @DisplayName("JUnit test for getVote method with error")
    @Test
    public void getVoteWithErrorTest() {
        when(voteRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(VoteNotFoundException.class, () -> voteService.getVote(savedVote.getId()));

        verify(voteRepository).findById(1L);
    }

    @DisplayName("JUnit test for getAllByQuote method")
    @Test
    public void getAllByQuoteTest() {
        when(voteRepository.findAllByQuote(quote.getId())).thenReturn(List.of(vote));
        List<VoteEntity> actual = voteService.getAllByQuote(quote.getId());

        assertEquals(List.of(savedVote), actual);
        verify(voteRepository).findAllByQuote(quote.getId());
    }

    @DisplayName("JUnit test for getAllActiveByQuote method")
    @Test
    public void getAllActiveByQuoteTest() {
        when(voteRepository.findAllActiveByQuote(quote.getId())).thenReturn(List.of(vote));
        List<VoteEntity> actual = voteService.getAllActiveByQuote(quote.getId());

        assertEquals(List.of(savedVote), actual);
        verify(voteRepository).findAllActiveByQuote(quote.getId());
    }
}