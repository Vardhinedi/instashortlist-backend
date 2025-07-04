package com.instashortlist.backend.repository;

import com.instashortlist.backend.model.CandidateStep;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface CandidateStepRepository extends ReactiveCrudRepository<CandidateStep, Long> {
    Flux<CandidateStep> findByCandidateIdOrderByStepOrderAsc(Long candidateId);
}
