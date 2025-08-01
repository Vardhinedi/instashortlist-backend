package com.instashortlist.backend.repository;

import com.instashortlist.backend.model.CandidateStep;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CandidateStepRepository extends ReactiveCrudRepository<CandidateStep, Long> {
    Flux<CandidateStep> findByCandidateIdOrderByStepOrderAsc(Long candidateId);

    // NEW: Delete all steps for a candidate before inserting fresh ones
    Mono<Void> deleteByCandidateId(Long candidateId);
}
