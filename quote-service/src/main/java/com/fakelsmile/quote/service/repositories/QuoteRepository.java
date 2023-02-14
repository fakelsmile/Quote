package com.fakelsmile.quote.service.repositories;

import com.fakelsmile.quote.service.models.entity.QuoteEntity;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for QuoteEntity.
 */
@Repository
public interface QuoteRepository extends PagingAndSortingRepository<QuoteEntity, Long> {
}