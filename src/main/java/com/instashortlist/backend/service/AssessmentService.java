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

    // ❌ (Deprecated): Generate from role – now disabled
    public Flux<Assessment> generateAssessmentsFromTemplate(String role, Long jobId) {
        return Flux.empty(); // No longer used – only selected templateIds are used
    }

    // ✅ Create from selected template IDs
    public Flux<Assessment> createAssessmentsFromTemplateIds(List<Long> templateIds, Long jobId) {
        return assessmentTemplateRepository.findAllById(templateIds)
                .index() // adds (index, template)
                .map(tuple -> {
                    long index = tuple.getT1();
                    AssessmentTemplate template = tuple.getT2();

                    Assessment a = new Assessment();
                    a.setJobId(jobId);
                    a.setStepOrder((int) index + 1); // maintain order from frontend
                    a.setQuestion(template.getStepName()); // take step name as question
                    
                    return a;
                })
                .collectList()
                .flatMapMany(assessmentRepository::saveAll);
    }
}
