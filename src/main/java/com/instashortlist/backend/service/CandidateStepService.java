package com.instashortlist.backend.service;

import com.instashortlist.backend.dto.UpdateStatusRequest;
import com.instashortlist.backend.model.Assessment;
import com.instashortlist.backend.model.CandidateStep;
import com.instashortlist.backend.repository.AssessmentRepository;
import com.instashortlist.backend.repository.CandidateStepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CandidateStepService {

    @Autowired
    private CandidateStepRepository candidateStepRepository;

    @Autowired
    private AssessmentRepository assessmentRepository;

    public Mono<CandidateStep> updateStepStatus(Long stepId, UpdateStatusRequest request) {
        return candidateStepRepository.findById(stepId)
                .flatMap(step -> {
                    step.setStatus(request.getStatus());
                    if (request.getCompleted() != null) {
                        step.setCompleted(request.getCompleted());
                    }
                    return candidateStepRepository.save(step);
                });
    }

    public Flux<CandidateStep> generateStepsForCandidate(Long candidateId, Long jobId) {
        return assessmentRepository.findByJobIdOrderByStepOrderAsc(jobId)
                .map(assessment -> {
                    CandidateStep step = new CandidateStep();
                    step.setCandidateId(candidateId);
                    step.setAssessmentId(assessment.getId());
                    step.setStepOrder(assessment.getStepOrder());
                    step.setStepName(assessment.getType()); // Or any other label
                    step.setStatus("PENDING");
                    step.setCompleted(false);
                    return step;
                })
                .flatMap(candidateStepRepository::save);
    }

    // ✅ NEW METHOD: Call this after a candidate applies to a job
    public Mono<Void> createCandidateStepsFromAssessments(Long candidateId, Long jobId) {
        return assessmentRepository.findByJobIdOrderByStepOrderAsc(jobId)
                .flatMap(assessment -> {
                    CandidateStep step = new CandidateStep();
                    step.setCandidateId(candidateId);
                    step.setAssessmentId(assessment.getId());
                    step.setStepOrder(assessment.getStepOrder());
                    step.setStepName(assessment.getType());
                    step.setStatus("PENDING");
                    step.setCompleted(false);
                    return candidateStepRepository.save(step);
                })
                .then(); // Return Mono<Void> after all steps are saved
    }
}
