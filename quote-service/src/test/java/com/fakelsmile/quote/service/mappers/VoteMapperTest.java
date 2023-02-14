package com.fakelsmile.quote.service.mappers;

import com.fakelsmile.quote.service.models.dto.user.UserShortDTO;
import com.fakelsmile.quote.service.models.dto.vote.HistoryVoteDTO;
import com.fakelsmile.quote.service.models.dto.vote.RequestVoteDTO;
import com.fakelsmile.quote.service.models.dto.vote.ResponseVoteDTO;
import com.fakelsmile.quote.service.models.entity.QuoteEntity;
import com.fakelsmile.quote.service.models.entity.UserEntity;
import com.fakelsmile.quote.service.models.entity.VoteEntity;
import com.fakelsmile.quote.service.models.entity.VoteType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VoteMapperTest {
    @InjectMocks
    private VoteMapperImpl voteMapper;
    @Mock
    private UserMapper userMapper;
    private UserEntity userEntity;
    private UserShortDTO userShortDTO;
    private QuoteEntity quoteEntity;
    private VoteEntity voteEntity;
    private RequestVoteDTO requestVoteDTO;

    @BeforeEach
    public void setUp() {
        userEntity = UserEntity.builder()
                .id(1L)
                .name("Zaph")
                .email("Zaph@gmail")
                .password("qwerty123")
                .quotes(List.of())
                .build();

        userShortDTO = new UserShortDTO();
        userShortDTO.setId(1L);
        userShortDTO.setName("Zaph");

        quoteEntity = QuoteEntity.builder()
                .id(1L)
                .content("Ramesh")
                .author(userEntity)
                .build();

        voteEntity = VoteEntity.builder()
                .id(1L)
                .user(userEntity)
                .quote(quoteEntity)
                .voteType(VoteType.UP)
                .isActive(true)
                .createAt(Instant.now())
                .updateAt(Instant.now())
                .build();

        requestVoteDTO = new RequestVoteDTO();
        requestVoteDTO.setId(1L);
        requestVoteDTO.setUserId(1L);
        requestVoteDTO.setQuoteId(1L);
        requestVoteDTO.setVoteType(VoteType.UP);
    }

    @AfterEach
    public void turnDown() {
        verifyNoMoreInteractions(userMapper);
    }

    @DisplayName("JUnit test for map voteEntity to voteDTO")
    @Test
    void toDTOFromVoteEntityTest() {
        ResponseVoteDTO expected = new ResponseVoteDTO();
        expected.setId(voteEntity.getId());
        expected.setUser(userShortDTO);
        expected.setVoteType(voteEntity.getVoteType());

        when(userMapper.toDto(voteEntity.getUser())).thenReturn(userShortDTO);
        ResponseVoteDTO result = voteMapper.toDto(voteEntity);

        assertNotNull(result);
        assertEquals(expected, result);
        verify(userMapper).toDto(voteEntity.getUser());
    }

    @DisplayName("JUnit test for map voteDTO to voteEntity")
    @Test
    void toEntityFromVoteDTOTest() {
        VoteEntity expected = new VoteEntity();
        expected.setId(requestVoteDTO.getId());
        expected.setUser(userEntity);
        expected.setQuote(quoteEntity);
        expected.setVoteType(requestVoteDTO.getVoteType());
        expected.setIsActive(true);

        VoteEntity actual = voteMapper.toEntity(requestVoteDTO);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @DisplayName("JUnit test for map voteEntity to toListDTO method")
    @Test
    void toListDTOFromQuoteEntityTest() {
        ResponseVoteDTO responseVoteDTO = new ResponseVoteDTO();
        responseVoteDTO.setId(voteEntity.getId());
        responseVoteDTO.setUser(userShortDTO);
        responseVoteDTO.setVoteType(voteEntity.getVoteType());

        List<ResponseVoteDTO> expected = List.of(responseVoteDTO);
        List<VoteEntity> voteEntityList = List.of(voteEntity);

        when(userMapper.toDto(voteEntity.getUser())).thenReturn(userShortDTO);
        List<ResponseVoteDTO> actual = voteMapper.toListDto(voteEntityList);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(expected, actual);
        verify(userMapper).toDto(voteEntity.getUser());
    }

    @DisplayName("JUnit test for map voteEntity to toHistoryListDTO method")
    @Test
    void toHistoryListDTOFromQuoteEntityTest() {
        HistoryVoteDTO historyVoteDTO = new HistoryVoteDTO();
        historyVoteDTO.setId(voteEntity.getId());
        historyVoteDTO.setUser(userShortDTO);
        historyVoteDTO.setVoteType(voteEntity.getVoteType());
        historyVoteDTO.setCreateAt(voteEntity.getCreateAt());
        historyVoteDTO.setUpdateAt(voteEntity.getUpdateAt());

        List<HistoryVoteDTO> expected = List.of(historyVoteDTO);
        List<VoteEntity> voteEntityList = List.of(voteEntity);

        when(userMapper.toDto(voteEntity.getUser())).thenReturn(userShortDTO);
        List<HistoryVoteDTO> actual = voteMapper.toHistoryListDto(voteEntityList);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(expected, actual);
        verify(userMapper).toDto(voteEntity.getUser());
    }
}
