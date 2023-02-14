package com.fakelsmile.quote.service.controllers;

import com.fakelsmile.quote.service.TestUtil;
import com.fakelsmile.quote.service.mappers.QuoteMapper;
import com.fakelsmile.quote.service.models.dto.*;
import com.fakelsmile.quote.service.models.dto.quote.QuoteSaveDTO;
import com.fakelsmile.quote.service.models.dto.quote.QuoteUpdateDTO;
import com.fakelsmile.quote.service.models.dto.quote.ResponseQuoteDTO;
import com.fakelsmile.quote.service.models.dto.user.UserShortDTO;
import com.fakelsmile.quote.service.models.entity.QuoteEntity;
import com.fakelsmile.quote.service.models.entity.UserEntity;
import com.fakelsmile.quote.service.services.QuoteService;
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

import java.util.List;
import java.util.Optional;

import static com.fakelsmile.quote.service.TestUtil.asJsonString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QuoteController.class)
public class QuoteControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private QuoteService quoteService;
    @MockBean
    private QuoteMapper quoteMapper;
    private UserEntity userEntity;
    private UserShortDTO userShortDTO;
    private QuoteEntity quoteEntity;
    private QuoteEntity savedQuoteEntity;
    private QuoteSaveDTO quoteSaveDTO;
    private QuoteUpdateDTO quoteUpdateDTO;

    @BeforeEach
    public void setUp() {
        userEntity = new UserEntity();
        userEntity.setId(3L);
        userEntity.setName("Rame");

        userShortDTO = new UserShortDTO();
        userShortDTO.setId(3L);
        userShortDTO.setName("Ramesh");

        quoteEntity = QuoteEntity.builder()
                .id(3L)
                .content("Ramesh")
                .author(userEntity)
                .build();

        savedQuoteEntity = QuoteEntity.builder()
                .id(3L)
                .content("Ramesh")
                .build();

        quoteSaveDTO = new QuoteSaveDTO();
        quoteSaveDTO.setContent("Ramesh");
        quoteSaveDTO.setAuthorId(3L);

        quoteUpdateDTO = new QuoteUpdateDTO();
        quoteUpdateDTO.setContent("Ramesh");
    }

    @AfterEach
    public void turnDown() {
        verifyNoMoreInteractions(quoteMapper, quoteService);
    }

    @DisplayName("JUnit test for getAllQuotes method")
    @Test
    public void getAllQuotesTest() throws Exception {
        UserShortDTO userShortDTO = new UserShortDTO();
        userShortDTO.setId(userEntity.getId());
        userShortDTO.setName(userEntity.getName());

        ResponseQuoteDTO responseQuoteDTO = new ResponseQuoteDTO();
        responseQuoteDTO.setId(3L);
        responseQuoteDTO.setContent("Ramesh");
        responseQuoteDTO.setAuthor(userShortDTO);

        List<QuoteEntity> quoteEntityList = List.of(quoteEntity);

        List<ResponseQuoteDTO> expected = List.of(responseQuoteDTO);

        when(quoteService.getAllQuotes()).thenReturn(quoteEntityList);
        when(quoteMapper.toListDto(quoteEntityList)).thenReturn(expected);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/quotes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        List<ResponseQuoteDTO> actual = TestUtil.objectMapper.readValue(jsonResult, new TypeReference<>() { });

        assertEquals(expected, actual);
        verify(quoteService).getAllQuotes();
        verify(quoteMapper).toListDto(quoteEntityList);
    }

    @DisplayName("JUnit test for createQuote method")
    @Test
    public void createQuoteTest() throws Exception {
        ResponseQuoteDTO expected = new ResponseQuoteDTO();
        expected.setId(1L);
        expected.setContent("Ramesh");

        when(quoteMapper.toEntity(quoteSaveDTO)).thenReturn(quoteEntity);
        when(quoteService.saveQuote(quoteEntity)).thenReturn(savedQuoteEntity);
        when(quoteMapper.toDto(savedQuoteEntity)).thenReturn(expected);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/quotes")
                        .content(asJsonString(quoteSaveDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        ResponseQuoteDTO actual = TestUtil.objectMapper.readValue(jsonResult, ResponseQuoteDTO.class);

        assertEquals(expected, actual);
        verify(quoteMapper).toEntity(quoteSaveDTO);
        verify(quoteService).saveQuote(quoteEntity);
        verify(quoteMapper).toDto(savedQuoteEntity);
    }

    @DisplayName("Should return the HTTP status code bad request (400) if Content is null")
    @Test
    public void createQuoteWithContentIsNullTest() throws Exception {
        QuoteSaveDTO expected = new QuoteSaveDTO();
        expected.setContent(null);
        expected.setAuthorId(2L);

        ErrorDTO errorDTO = new ErrorDTO("content", "Content cannot be empty");
        List<ErrorDTO> errors = List.of(errorDTO);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/quotes")
                        .content(asJsonString(expected))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        List<ErrorDTO> actual = TestUtil.objectMapper.readValue(jsonResult, new TypeReference<>() {});
        assertEquals(errors, actual);
    }

    @DisplayName("JUnit test for getQuote method")
    @Test
    public void getQuoteTest() throws Exception {
        ResponseQuoteDTO expected = new ResponseQuoteDTO();
        expected.setId(1L);
        expected.setContent("Ramesh");

        when(quoteService.getQuote(quoteEntity.getId())).thenReturn(quoteEntity);
        when(quoteMapper.toDto(quoteEntity)).thenReturn(expected);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/quotes/" + quoteEntity.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        ResponseQuoteDTO actual = TestUtil.objectMapper.readValue(jsonResult, ResponseQuoteDTO.class);

        assertEquals(expected, actual);
        verify(quoteService).getQuote(quoteEntity.getId());
        verify(quoteMapper).toDto(quoteEntity);
    }

    @DisplayName("JUnit test for updateQuote method")
    @Test
    public void updateQuoteTest() throws Exception {
        ResponseQuoteDTO expected = new ResponseQuoteDTO();
        expected.setId(1L);
        expected.setContent("Ramesh");
        expected.setScore(0L);
        expected.setAuthor(userShortDTO);

        when(quoteMapper.toEntity(quoteUpdateDTO)).thenReturn(quoteEntity);
        when(quoteService.updateQuote(quoteEntity.getId(), quoteEntity)).thenReturn(savedQuoteEntity);
        when(quoteMapper.toDto(savedQuoteEntity)).thenReturn(expected);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/quotes/" + quoteEntity.getId())
                        .content(asJsonString(quoteUpdateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        ResponseQuoteDTO actual = TestUtil.objectMapper.readValue(jsonResult, ResponseQuoteDTO.class);

        assertEquals(expected, actual);
        verify(quoteMapper).toEntity(quoteUpdateDTO);
        verify(quoteService).updateQuote(quoteEntity.getId(), quoteEntity);
        verify(quoteMapper).toDto(savedQuoteEntity);
    }

    @DisplayName("JUnit test for deleteQuote method")
    @Test
    public void deleteQuoteTest() throws Exception {
        long quoteId = 3L;

        mvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/quotes/" + quoteId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(quoteService).deleteQuote(quoteId);
    }

    @DisplayName("JUnit test for getRandomQuote method")
    @Test
    public void getRandomQuoteTest() throws Exception {
        ResponseQuoteDTO responseQuoteDTO = new ResponseQuoteDTO();
        responseQuoteDTO.setContent("Ramesh");

        ResponseQuoteDTO expected = new ResponseQuoteDTO();
        expected.setId(1L);
        expected.setContent("Ramesh");

        when(quoteService.getRandomQuote()).thenReturn(Optional.of(quoteEntity));
        when(quoteMapper.toDto(quoteEntity)).thenReturn(expected);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/quotes/random")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        ResponseQuoteDTO actual = TestUtil.objectMapper.readValue(jsonResult, ResponseQuoteDTO.class);

        assertEquals(expected, actual);
        verify(quoteService).getRandomQuote();
        verify(quoteMapper).toDto(quoteEntity);
    }

    @DisplayName("JUnit test for getQuotesOrderByScore method")
    @Test
    public void getQuotesOrderByScoreTest() throws Exception {
        UserShortDTO userShortDTO = new UserShortDTO();
        userShortDTO.setId(userEntity.getId());
        userShortDTO.setName(userEntity.getName());

        ResponseQuoteDTO responseQuoteDTO = new ResponseQuoteDTO();
        responseQuoteDTO.setId(3L);
        responseQuoteDTO.setContent("Ramesh");
        responseQuoteDTO.setAuthor(userShortDTO);

        List<ResponseQuoteDTO> expected = List.of(responseQuoteDTO);

        Integer limit = 10;
        DirectionType directionType = DirectionType.ASC;

        when(quoteService.getQuotesOrderByScore(directionType, limit)).thenReturn(List.of(quoteEntity));
        when(quoteMapper.toListDto(List.of(quoteEntity))).thenReturn(expected);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/quotes/sort")
                        .param("limit", limit.toString())
                        .param("directionType", directionType.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        List<ResponseQuoteDTO> actual = TestUtil.objectMapper.readValue(jsonResult, new TypeReference<>() { });

        assertEquals(expected, actual);
        verify(quoteService).getQuotesOrderByScore(directionType, limit);
        verify(quoteMapper).toListDto(List.of(quoteEntity));
    }
}
