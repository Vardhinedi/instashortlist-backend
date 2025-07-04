package com.instashortlist.backend.repository;

import com.instashortlist.backend.model.Assessment;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface AssessmentRepository extends ReactiveCrudRepository<Assessment, Long> {
    Flux<Assessment> findByJobIdOrderByStepOrderAsc(Long jobId);
}
