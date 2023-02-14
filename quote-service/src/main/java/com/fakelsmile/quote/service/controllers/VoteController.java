package com.fakelsmile.quote.service.controllers;

import com.fakelsmile.quote.service.errors.NotFoundException;
import com.fakelsmile.quote.service.errors.QuoteNotFoundException;
import com.fakelsmile.quote.service.errors.VoteAlreadyExistException;
import com.fakelsmile.quote.service.errors.VoteNotFoundException;
import com.fakelsmile.quote.service.mappers.VoteMapper;
import com.fakelsmile.quote.service.models.dto.vote.HistoryVoteDTO;
import com.fakelsmile.quote.service.models.dto.vote.RequestVoteDTO;
import com.fakelsmile.quote.service.models.dto.vote.ResponseVoteDTO;
import com.fakelsmile.quote.service.models.entity.VoteEntity;
import com.fakelsmile.quote.service.models.entity.VoteType;
import com.fakelsmile.quote.service.services.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Controller for votes.
 */
@Validated
@RestController
@RequestMapping(path = "/api/v1/votes")
@RequiredArgsConstructor
public class VoteController {
    private final VoteService service;
    private final VoteMapper mapper;

    /**
     * Create a new vote.
     *
     * @param requestVoteDTO - vote
     * @return created vote
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseVoteDTO createVote(@Valid @RequestBody RequestVoteDTO requestVoteDTO) throws VoteAlreadyExistException, QuoteNotFoundException {
        VoteEntity voteEntity = mapper.toEntity(requestVoteDTO);
        voteEntity = service.saveVote(voteEntity);
        return mapper.toDto(voteEntity);
    }

    /**
     * Get a vote by id.
     *
     * @param id - vote id
     * @return found vote
     * @throws VoteNotFoundException if the vote is not found
     */
    @GetMapping("/{id}")
    public ResponseVoteDTO getVote(@PathVariable Long id) throws VoteNotFoundException {
        VoteEntity voteEntityId = service.getVote(id);
        return mapper.toDto(voteEntityId);
    }

    /**
     * Update a vote by id.
     *
     * @param id       - vote id
     * @param voteType - type of vote
     * @return updated vote
     * @throws VoteNotFoundException if the vote is not found
     */
    @PutMapping("/{id}")
    public ResponseVoteDTO updateVote(@NotNull @RequestBody VoteType voteType, @PathVariable Long id) throws NotFoundException {
        VoteEntity voteEntity = service.updateVote(id, voteType);
        return mapper.toDto(voteEntity);
    }

    /**
     * Delete a vote by id.
     *
     * @param id - vote id
     */
    @DeleteMapping("/{id}")
    public void deleteVote(@PathVariable Long id) throws NotFoundException {
        service.deleteVote(id);
    }

    /**
     * Get a history of votes by quote id.
     *
     * @param quoteId - quote id
     * @return found votes
     */
    @GetMapping("/history/{quoteId}")
    public List<HistoryVoteDTO> getHistory(@PathVariable Long quoteId) {
        List<VoteEntity> voteEntities = service.getAllByQuote(quoteId);
        return mapper.toHistoryListDto(voteEntities);
    }

    /**
     * Get all active votes by quote id.
     *
     * @param quoteId - quote id
     * @return list of active votes
     */
    @GetMapping("/active/{quoteId}")
    public List<ResponseVoteDTO> getAllActiveVotes(@PathVariable Long quoteId) {
        List<VoteEntity> quoteEntityList = service.getAllActiveByQuote(quoteId);
        return mapper.toListDto(quoteEntityList);
    }
}
