package com.instashortlist.backend.controller;

import com.instashortlist.backend.model.Assessment;
import com.instashortlist.backend.service.AssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/assessments")
@CrossOrigin(origins = "*") // ✅ Or restrict to Vercel URL if needed
public class AssessmentController {

    @Autowired
    private AssessmentService assessmentService;

    // Create one assessment step for a job
    @PostMapping
    public Mono<Assessment> createAssessment(@RequestBody Assessment assessment) {
        return assessmentService.createAssessmentStep(assessment);
    }

    // Get all assessment steps for a job by job ID
    @GetMapping("/job/{jobId}")
    public Flux<Assessment> getAssessmentSteps(@PathVariable Long jobId) {
        return assessmentService.getAssessmentStepsByJobId(jobId);
    }
}
