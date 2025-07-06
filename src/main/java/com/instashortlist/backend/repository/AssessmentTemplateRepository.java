package com.instashortlist.backend.repository;

import com.instashortlist.backend.model.AssessmentTemplate;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface AssessmentTemplateRepository extends ReactiveCrudRepository<AssessmentTemplate, Long> {
    Flux<AssessmentTemplate> findByRoleOrderByStepOrderAsc(String role);
}
