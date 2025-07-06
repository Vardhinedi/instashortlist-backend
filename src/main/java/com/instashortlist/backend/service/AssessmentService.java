package com.instashortlist.backend.service;

import com.instashortlist.backend.model.Assessment;
import com.instashortlist.backend.repository.AssessmentRepository;
import com.instashortlist.backend.repository.AssessmentTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AssessmentService {

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private DatabaseClient databaseClient;

    @Autowired
    private AssessmentTemplateRepository assessmentTemplateRepository;

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

    // 🔹 Generate assessments for a job using template
    public Flux<Assessment> generateAssessmentsFromTemplate(String role, Long jobId) {
        return assessmentTemplateRepository.findByRoleOrderByStepOrderAsc(role)
                .map(template -> {
                    Assessment assessment = new Assessment();
                    assessment.setJobId(jobId);
                    assessment.setQuestion(template.getQuestion());
                    assessment.setOptions(template.getOptions());
                    assessment.setCorrectAnswer(template.getCorrectAnswer());
                    assessment.setType(template.getType());
                    assessment.setStepOrder(template.getStepOrder());
                    return assessment;
                })
                .flatMap(assessmentRepository::save);
    }
}
