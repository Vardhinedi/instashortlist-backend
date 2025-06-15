package com.instashortlist.backend.repository;

import com.instashortlist.backend.model.Opportunity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpportunityRepository extends ReactiveCrudRepository<Opportunity, Long> {
}
