package com.fakelsmile.quote.service.mappers;

import com.fakelsmile.quote.service.models.dto.vote.HistoryVoteDTO;
import com.fakelsmile.quote.service.models.dto.vote.RequestVoteDTO;
import com.fakelsmile.quote.service.models.dto.vote.ResponseVoteDTO;
import com.fakelsmile.quote.service.models.entity.VoteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for votes.
 */
@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface VoteMapper {

    /**
     * Map VoteDTO to VoteEntity.
     *
     * @param requestVoteDTO - vote DTO
     * @return mapped voteEntity
     */
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "quote.id", source = "quoteId")
    @Mapping(target = "isActive", constant = "true")
    VoteEntity toEntity(RequestVoteDTO requestVoteDTO);

    /**
     * Map VoteEntity to VoteDTO.
     *
     * @param voteEntity - vote entity
     * @return mapped voteDTO
     */
    ResponseVoteDTO toDto(VoteEntity voteEntity);

    /**
     * Map List of VoteEntity to List of HistoryVoteDTO.
     *
     * @param voteEntities - list of vote entities
     * @return mapped list of HistoryVoteDTO
     */
    List<HistoryVoteDTO> toHistoryListDto(List<VoteEntity> voteEntities);

    /**
     * Map List of VoteEntity to List of ResponseVoteDTO.
     *
     * @param voteEntities - list of vote entities
     * @return mapped list of ResponseVoteDTO
     */
    List<ResponseVoteDTO> toListDto(List<VoteEntity> voteEntities);
}

