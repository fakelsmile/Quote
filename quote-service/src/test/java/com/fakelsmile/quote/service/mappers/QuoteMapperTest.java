package com.fakelsmile.quote.service.mappers;

import com.fakelsmile.quote.service.models.dto.quote.QuoteSaveDTO;
import com.fakelsmile.quote.service.models.dto.quote.QuoteUpdateDTO;
import com.fakelsmile.quote.service.models.dto.quote.ResponseQuoteDTO;
import com.fakelsmile.quote.service.models.dto.user.UserShortDTO;
import com.fakelsmile.quote.service.models.entity.QuoteEntity;
import com.fakelsmile.quote.service.models.entity.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuoteMapperTest {

    @InjectMocks
    private QuoteMapperImpl quoteMapper;
    @Mock
    private UserMapper userMapper;
    private UserEntity userEntity;
    private UserShortDTO userShortDTO;
    private QuoteEntity quoteEntity;
    private ResponseQuoteDTO responseQuoteDTO;
    private QuoteSaveDTO quoteSaveDTO;
    private QuoteUpdateDTO quoteUpdateDTO;

    @BeforeEach
    public void setUp() {
        userEntity = new UserEntity();
        userEntity.setId(3L);
        userEntity.setName("Rameshhh");
        userEntity.setEmail("ramesh@gmail.com");
        userEntity.setPassword("Fadatare");

        userShortDTO = new UserShortDTO();
        userShortDTO.setId(userEntity.getId());
        userShortDTO.setName(userEntity.getName());

        quoteEntity = QuoteEntity.builder()
                .id(3L)
                .content("Ramesh")
                .score(0L)
                .author(userEntity)
                .build();

        responseQuoteDTO = new ResponseQuoteDTO();
        responseQuoteDTO.setId(1L);
        responseQuoteDTO.setContent("Ramesh");

        quoteSaveDTO = new QuoteSaveDTO();
        quoteSaveDTO.setContent(responseQuoteDTO.getContent());
        quoteSaveDTO.setAuthorId(userEntity.getId());

        quoteUpdateDTO = new QuoteUpdateDTO();
        quoteUpdateDTO.setContent(responseQuoteDTO.getContent());
    }

    @AfterEach
    public void turnDown() {
        verifyNoMoreInteractions(userMapper);
    }

    @DisplayName("JUnit test for map quoteDTO to quoteEntity method")
    @Test
    void toEntityFromQuoteSaveDTOTest() {
        QuoteEntity expected = new QuoteEntity();
        expected.setContent(quoteSaveDTO.getContent());
        expected.setScore(0L);
        expected.setAuthor(userEntity);

        QuoteEntity actual = quoteMapper.toEntity(quoteSaveDTO);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @DisplayName("JUnit test for map quoteDTO to quoteEntity method")
    @Test
    void toEntityFromQuoteUpdateDTOTest() {
        QuoteEntity expected = new QuoteEntity();
        expected.setContent(responseQuoteDTO.getContent());
        expected.setAuthor(userEntity);

        QuoteEntity actual = quoteMapper.toEntity(quoteUpdateDTO);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @DisplayName("JUnit test for map quoteEntity to toListDTO method")
    @Test
    void toListDTOFromQuoteEntityTest() {
        ResponseQuoteDTO responseQuoteDTO = new ResponseQuoteDTO();
        responseQuoteDTO.setId(quoteEntity.getId());
        responseQuoteDTO.setContent(quoteEntity.getContent());
        responseQuoteDTO.setScore(quoteEntity.getScore());
        responseQuoteDTO.setAuthor(userShortDTO);

        List<ResponseQuoteDTO> expected = List.of(responseQuoteDTO);
        List<QuoteEntity> quoteEntityList = List.of(quoteEntity);

        when(userMapper.toDto(quoteEntity.getAuthor())).thenReturn(userShortDTO);
        List<ResponseQuoteDTO> actual = quoteMapper.toListDto(quoteEntityList);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(expected, actual);
        verify(userMapper).toDto(quoteEntity.getAuthor());
    }

    @DisplayName("JUnit test for map toDTOFromQuoteEntity to responseQuoteDTO method")
    @Test
    void toDTOFromQuoteEntityTest() {
        ResponseQuoteDTO expected = new ResponseQuoteDTO();
        expected.setId(quoteEntity.getId());
        expected.setContent(quoteEntity.getContent());
        expected.setScore(quoteEntity.getScore());
        expected.setAuthor(userShortDTO);

        when(userMapper.toDto(quoteEntity.getAuthor())).thenReturn(userShortDTO);
        ResponseQuoteDTO actual = quoteMapper.toDto(quoteEntity);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(userMapper).toDto(quoteEntity.getAuthor());
    }
}
