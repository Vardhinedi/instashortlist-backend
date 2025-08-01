package com.instashortlist.backend.service;

import com.instashortlist.backend.dto.CandidateStepWithReviewDTO;
import com.instashortlist.backend.dto.CandidateStepsResponseDTO;
import com.instashortlist.backend.dto.UpdateStatusRequest;
import com.instashortlist.backend.model.CandidateStep;
import com.instashortlist.backend.repository.AssessmentRepository;
import com.instashortlist.backend.repository.CandidateRepository;
import com.instashortlist.backend.repository.CandidateStepRepository;
import com.instashortlist.backend.repository.ReviewRepository;
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

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CandidateRepository candidateRepository;

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

    public Mono<Void> createCandidateStepsFromAssessments(Long candidateId, Long jobId) {
        return candidateStepRepository.findByCandidateIdOrderByStepOrderAsc(candidateId)
                .flatMap(candidateStepRepository::delete)
                .thenMany(assessmentRepository.findByJobIdOrderByStepOrderAsc(jobId))
                .flatMap(assessment -> {
                    CandidateStep step = new CandidateStep();
                    step.setCandidateId(candidateId);
                    step.setAssessmentId(assessment.getId());
                    step.setStepOrder(assessment.getStepOrder());
                    step.setStepName(assessment.getStepName() != null ? assessment.getStepName() : "Untitled Step");
                    step.setStatus("PENDING");
                    step.setCompleted(false);
                    return candidateStepRepository.save(step);
                })
                .then();
    }

    public Flux<CandidateStepWithReviewDTO> getStepsWithReviewScoreByCandidateId(Long candidateId) {
        return candidateStepRepository.findByCandidateIdOrderByStepOrderAsc(candidateId)
                .flatMap(step ->
                        reviewRepository.findAll()
                                .filter(review -> review.getStepId().equals(step.getId()))
                                .next()
                                .map(review -> new CandidateStepWithReviewDTO(
                                        step.getId(),
                                        step.getCandidateId(),
                                        step.getAssessmentId(),
                                        step.getStepOrder(),
                                        step.getStepName(),
                                        step.getStatus(),
                                        step.getCompleted(),
                                        review.getScore()
                                ))
                                .defaultIfEmpty(new CandidateStepWithReviewDTO(
                                        step.getId(),
                                        step.getCandidateId(),
                                        step.getAssessmentId(),
                                        step.getStepOrder(),
                                        step.getStepName(),
                                        step.getStatus(),
                                        step.getCompleted(),
                                        null
                                ))
                );
    }

    // ✅ Updated: Only return steps, no averageScore
    public Mono<CandidateStepsResponseDTO> getStepsAndAverageScore(Long candidateId) {
        return getStepsWithReviewScoreByCandidateId(candidateId)
                .collectList()
                .map(CandidateStepsResponseDTO::new); // use constructor that takes only steps
    }

    public Mono<Void> updateCandidateOverallScore(Long candidateId) {
        return getStepsWithReviewScoreByCandidateId(candidateId)
                .filter(CandidateStepWithReviewDTO::getCompleted)
                .filter(step -> step.getScore() != null)
                .collectList()
                .flatMap(steps -> {
                    if (steps.isEmpty()) {
                        return candidateRepository.findById(candidateId)
                                .flatMap(candidate -> {
                                    candidate.setScore(0);
                                    return candidateRepository.save(candidate);
                                }).then();
                    }

                    double average = steps.stream()
                            .mapToInt(CandidateStepWithReviewDTO::getScore)
                            .average()
                            .orElse(0.0);

                    int averageRounded = (int) Math.round(average);

                    return candidateRepository.findById(candidateId)
                            .flatMap(candidate -> {
                                candidate.setScore(averageRounded);
                                return candidateRepository.save(candidate);
                            }).then();
                });
    }
}
