package com.instashortlist.backend.service;

import com.instashortlist.backend.model.Assessment;
import com.instashortlist.backend.model.AssessmentTemplate;
import com.instashortlist.backend.repository.AssessmentRepository;
import com.instashortlist.backend.repository.AssessmentTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class AssessmentService {

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private AssessmentTemplateRepository assessmentTemplateRepository;

    // 🔹 Fetch all assessments (new method)
    public Flux<Assessment> getAllAssessments() {
        return assessmentRepository.findAll();
    }

    // 🔹 Fetch assessment steps for a job
    public Flux<Assessment> getAssessmentStepsByJobId(Long jobId) {
        return assessmentRepository.findByJobIdOrderByStepOrderAsc(jobId);
    }

    // 🔹 Create individual assessment step
    public Mono<Assessment> createAssessmentStep(Assessment assessment) {
        return assessmentRepository.save(assessment);
    }

    // 🔹 Create steps in bulk
    public Flux<Assessment> createAssessmentSteps(Flux<Assessment> steps) {
        return assessmentRepository.saveAll(steps);
    }

    // ❌ Deprecated: Generate from role
    public Flux<Assessment> generateAssessmentsFromTemplate(String role, Long jobId) {
        return Flux.empty(); // Deprecated
    }

    // ✅ Create from selected template IDs
    public Flux<Assessment> createAssessmentsFromTemplateIds(List<Long> templateIds, Long jobId) {
        return assessmentTemplateRepository.findAllById(templateIds)
                .index()
                .map(tuple -> {
                    long index = tuple.getT1();
                    AssessmentTemplate template = tuple.getT2();

                    Assessment a = new Assessment();
                    a.setStepOrder((int) index + 1);
                    a.setStepName(template.getStepName());
                    a.setMode(template.getMode());
                    a.setPassingCriteria(Integer.valueOf(template.getPassingCriteria())); // Fixed: Convert String to Integer

                    return a;
                })
                .collectList()
                .flatMapMany(assessmentRepository::saveAll);
    }

    // ✅ Existing: Link a job to a single assessment step
    public Mono<Void> linkJobToAssessment(Long assessmentId, Long jobId, Integer stepOrder) {
        return assessmentRepository
                .linkJobToAssessment(jobId, assessmentId) // now returns Mono<Integer>
                .then(); // ✅ ignore the row count
    }

    // ✅ New: Link a job to ALL steps of an assessment by title
    public Flux<Assessment> linkJobToAssessmentByTitle(String title, Long jobId) {
        return assessmentRepository.findByTitleOrderByStepOrderAsc(title)
                .flatMap(step -> assessmentRepository
                        .linkJobToAssessment(jobId, step.getId())
                        .thenReturn(step));
    }
}