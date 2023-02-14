package com.fakelsmile.quote.service.controllers;

import com.fakelsmile.quote.service.TestUtil;
import com.fakelsmile.quote.service.mappers.VoteMapper;
import com.fakelsmile.quote.service.models.dto.user.UserShortDTO;
import com.fakelsmile.quote.service.models.dto.vote.HistoryVoteDTO;
import com.fakelsmile.quote.service.models.dto.vote.RequestVoteDTO;
import com.fakelsmile.quote.service.models.dto.vote.ResponseVoteDTO;
import com.fakelsmile.quote.service.models.entity.QuoteEntity;
import com.fakelsmile.quote.service.models.entity.UserEntity;
import com.fakelsmile.quote.service.models.entity.VoteEntity;
import com.fakelsmile.quote.service.models.entity.VoteType;
import com.fakelsmile.quote.service.services.VoteService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;
import java.util.List;

import static com.fakelsmile.quote.service.TestUtil.asJsonString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VoteController.class)
public class VoteControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private VoteService voteService;
    @MockBean
    private VoteMapper voteMapper;

    private VoteEntity voteEntity;
    private VoteEntity savedVoteEntity;
    private RequestVoteDTO requestVoteDTO;
    private ResponseVoteDTO responseVoteDTO;
    private UserShortDTO userShortDTO;
    private UserEntity userEntity;
    private QuoteEntity quoteEntity;

    @BeforeEach
    public void setUp() {
        userEntity= UserEntity.builder()
                .id(3L)
                .name("Ramesh")
                .build();
        quoteEntity = QuoteEntity.builder()
                .id(3L)
                .build();

        voteEntity = VoteEntity.builder()
                .id(3L)
                .user(userEntity)
                .quote(quoteEntity)
                .voteType(VoteType.UP)
                .build();

        requestVoteDTO = new RequestVoteDTO();
        requestVoteDTO.setId(1L);
        requestVoteDTO.setUserId(1L);
        requestVoteDTO.setQuoteId(1L);
        requestVoteDTO.setVoteType(VoteType.UP);

        savedVoteEntity = VoteEntity.builder()
                .id(3L)
                .user(userEntity)
                .quote(quoteEntity)
                .voteType(VoteType.UP)
                .build();

        userShortDTO = new UserShortDTO();
        userShortDTO.setId(3L);
        userShortDTO.setName("Ramesh");

        responseVoteDTO = new ResponseVoteDTO();
        responseVoteDTO.setId(3L);
        responseVoteDTO.setUser(userShortDTO);
        responseVoteDTO.setVoteType(VoteType.UP);
    }

    @AfterEach
    public void turnDown() {
        verifyNoMoreInteractions(voteMapper, voteService);
    }

    @DisplayName("JUnit test for createVote method")
    @Test
    public void createVoteTest() throws Exception {
        RequestVoteDTO request = new RequestVoteDTO();
        request.setId(1L);
        request.setUserId(1L);
        request.setQuoteId(1L);
        request.setVoteType(VoteType.UP);

        ResponseVoteDTO expected = new ResponseVoteDTO();
        expected.setId(3L);
        expected.setUser(userShortDTO);
        expected.setVoteType(VoteType.UP);

        when(voteMapper.toEntity(requestVoteDTO)).thenReturn(voteEntity);
        when(voteService.saveVote(voteEntity)).thenReturn(savedVoteEntity);
        when(voteMapper.toDto(savedVoteEntity)).thenReturn(responseVoteDTO);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/votes")
                        .content(asJsonString(requestVoteDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        ResponseVoteDTO actual = TestUtil.objectMapper.readValue(jsonResult, ResponseVoteDTO.class);

        assertEquals(expected, actual);
        verify(voteMapper).toEntity(requestVoteDTO);
        verify(voteService).saveVote(voteEntity);
        verify(voteMapper).toDto(savedVoteEntity);
    }

    @DisplayName("JUnit test for getVote method")
    @Test
    public void getVoteTest() throws Exception {
        RequestVoteDTO request = new RequestVoteDTO();
        request.setId(1L);
        request.setUserId(1L);
        request.setQuoteId(1L);
        request.setVoteType(VoteType.UP);

        ResponseVoteDTO expected = new ResponseVoteDTO();
        expected.setId(3L);
        expected.setUser(userShortDTO);
        expected.setVoteType(VoteType.UP);

        when(voteService.getVote(voteEntity.getId())).thenReturn(voteEntity);
        when(voteMapper.toDto(voteEntity)).thenReturn(responseVoteDTO);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/votes/" + voteEntity.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        ResponseVoteDTO actual = TestUtil.objectMapper.readValue(jsonResult, ResponseVoteDTO.class);

        assertEquals(expected, actual);
        verify(voteService).getVote(voteEntity.getId());
        verify(voteMapper).toDto(voteEntity);
    }

    @DisplayName("JUnit test for updateVote method")
    @Test
    public void updateVoteTest() throws Exception {
        RequestVoteDTO request = new RequestVoteDTO();
        request.setId(1L);
        request.setUserId(1L);
        request.setQuoteId(1L);
        request.setVoteType(VoteType.UP);

        ResponseVoteDTO expected = new ResponseVoteDTO();
        expected.setId(3L);
        expected.setUser(userShortDTO);
        expected.setVoteType(VoteType.UP);

        when(voteService.updateVote(voteEntity.getId(), voteEntity.getVoteType())).thenReturn(savedVoteEntity);
        when(voteMapper.toDto(savedVoteEntity)).thenReturn(responseVoteDTO);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/votes/" + voteEntity.getId())
                        .content(asJsonString(requestVoteDTO.getVoteType()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        ResponseVoteDTO actual = TestUtil.objectMapper.readValue(jsonResult, ResponseVoteDTO.class);
        assertEquals(expected, actual);
        verify(voteService).updateVote(voteEntity.getId(), voteEntity.getVoteType());
        verify(voteMapper).toDto(savedVoteEntity);
    }

    @DisplayName("JUnit test for deleteVote method")
    @Test
    public void deleteVoteTest() throws Exception {
        long voteId = 3L;

        mvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/votes/" + voteId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(voteService).deleteVote(voteId);
    }

    @DisplayName("JUnit test for getHistory method")
    @Test
    public void getHistoryTest() throws Exception {

        HistoryVoteDTO historyVoteDTO = new HistoryVoteDTO();
        historyVoteDTO.setId(3L);
        historyVoteDTO.setVoteType(VoteType.UP);
        historyVoteDTO.setUser(userShortDTO);
        historyVoteDTO.setCreateAt(Instant.now());
        historyVoteDTO.setUpdateAt(Instant.now());

        List<HistoryVoteDTO> expected = List.of(historyVoteDTO);

        when(voteService.getAllByQuote(quoteEntity.getId())).thenReturn(List.of(voteEntity));
        when(voteMapper.toHistoryListDto(List.of(voteEntity))).thenReturn(List.of(historyVoteDTO));

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/votes/history/" + quoteEntity.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        List<HistoryVoteDTO> actual = TestUtil.objectMapper.readValue(jsonResult, new TypeReference<>() { });

        assertEquals(expected, actual);
        verify(voteService).getAllByQuote(quoteEntity.getId());
        verify(voteMapper).toHistoryListDto(List.of(voteEntity));
    }

    @DisplayName("JUnit test for getAllActiveVotes method")
    @Test
    public void getAllActiveVotesTest() throws Exception {
        when(voteService.getAllActiveByQuote(quoteEntity.getId())).thenReturn(List.of(voteEntity));
        when(voteMapper.toListDto(List.of(voteEntity))).thenReturn(List.of(responseVoteDTO));

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/votes/active/" + quoteEntity.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        List<ResponseVoteDTO> actual = TestUtil.objectMapper.readValue(jsonResult, new TypeReference<>() { });

        assertEquals(List.of(responseVoteDTO), actual);
        verify(voteService).getAllActiveByQuote(quoteEntity.getId());
        verify(voteMapper).toListDto(List.of(voteEntity));
    }
}

