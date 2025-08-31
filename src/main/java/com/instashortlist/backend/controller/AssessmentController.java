package com.instashortlist.backend.controller;

import com.instashortlist.backend.model.Assessment;
import com.instashortlist.backend.service.AssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/assessments")
@CrossOrigin(origins = "*") // ✅ Restrict in production
public class AssessmentController {

    @Autowired
    private AssessmentService assessmentService;

    // 🔹 Create one assessment step independently
    @PostMapping
    public Mono<Assessment> createAssessment(@RequestBody Assessment assessment) {
        return assessmentService.createAssessmentStep(assessment);
    }

    // 🔹 Create multiple assessment steps independently
    @PostMapping("/bulk")
    public Flux<Assessment> createAssessmentSteps(@RequestBody Flux<Assessment> assessments) {
        return assessmentService.createAssessmentSteps(assessments);
    }

    // 🔹 Get all assessment steps for a job by job ID
    @GetMapping("/job/{jobId}")
    public Flux<Assessment> getAssessmentSteps(@PathVariable Long jobId) {
        return assessmentService.getAssessmentStepsByJobId(jobId);
    }

    // ✅ New: Get all assessments (no job filter)
    @GetMapping
    public Flux<Assessment> getAllAssessments() {
        return assessmentService.getAllAssessments();
    }

    // ✅ Existing: Link a job to a single assessment step (by ID)
    @PostMapping("/link/{assessmentId}/job/{jobId}")
    public Mono<ResponseEntity<Void>> linkJobToAssessment(
            @PathVariable Long assessmentId,
            @PathVariable Long jobId,
            @RequestBody(required = false) LinkRequest linkRequest) {

        return assessmentService.linkJobToAssessment(
                        assessmentId,
                        jobId,
                        linkRequest != null ? linkRequest.getStepOrder() : null)
                .then(Mono.just(new ResponseEntity<>(HttpStatus.NO_CONTENT)));
    }

    // ✅ New: Link a job to ALL steps of an assessment by title
    @PostMapping("/link/title/{title}/job/{jobId}")
    public Flux<Assessment> linkJobToAssessmentByTitle(
            @PathVariable String title,
            @PathVariable Long jobId) {
        return assessmentService.linkJobToAssessmentByTitle(title, jobId);
    }

    // DTO for optional link details
    public static class LinkRequest {
        private Integer stepOrder;
        public Integer getStepOrder() { return stepOrder; }
        public void setStepOrder(Integer stepOrder) { this.stepOrder = stepOrder; }
    }
}