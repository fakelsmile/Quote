package com.fakelsmile.quote.service.mappers;

import com.fakelsmile.quote.service.models.dto.quote.QuoteSaveDTO;
import com.fakelsmile.quote.service.models.dto.quote.QuoteUpdateDTO;
import com.fakelsmile.quote.service.models.dto.quote.ResponseQuoteDTO;
import com.fakelsmile.quote.service.models.entity.QuoteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.util.List;

/**
 * Mapper for quotes.
 */
@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface QuoteMapper {

    /**
     * Map QuoteEntity to ResponseQuoteDTO.
     *
     * @param quoteEntity - quote entity
     * @return mapped quoteDTO
     */
    ResponseQuoteDTO toDto(QuoteEntity quoteEntity);

    /**
     * Map List of QuoteEntities to List of ResponseQuoteDTO.
     *
     * @param quoteEntity - quote entity
     * @return mapped ResponseQuoteDTO
     */
    List<ResponseQuoteDTO> toListDto(List<QuoteEntity> quoteEntity);

    /**
     * Map QuoteSaveDTO to QuoteEntity.
     *
     * @param quoteSaveDTO - DTO for save quote
     * @return mapped QuoteEntity
     */
    @Mapping(source = "authorId", target = "author.id")
    @Mapping(target = "score", constant = "0L")
    QuoteEntity toEntity(QuoteSaveDTO quoteSaveDTO);

    /**
     * Map QuoteUpdateDTO to QuoteEntity.
     *
     * @param quoteUpdateDTO - DTO for update quote
     * @return mapped QuoteEntity
     */
    QuoteEntity toEntity(QuoteUpdateDTO quoteUpdateDTO);
}
