package com.instashortlist.backend.service;

import com.instashortlist.backend.model.Assessment;
import com.instashortlist.backend.repository.AssessmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AssessmentService {

    @Autowired
    private AssessmentRepository assessmentRepository;

    // Get all steps of an assessment for a job
    public Flux<Assessment> getAssessmentStepsByJobId(Long jobId) {
        return assessmentRepository.findByJobIdOrderByStepOrderAsc(jobId);
    }

    // Create a new assessment step
    public Mono<Assessment> createAssessmentStep(Assessment assessment) {
        return assessmentRepository.save(assessment);
    }

    // Create multiple steps for a job (optional batch setup)
    public Flux<Assessment> createAssessmentSteps(Flux<Assessment> steps) {
        return assessmentRepository.saveAll(steps);
    }
}
