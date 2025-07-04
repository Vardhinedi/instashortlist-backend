package com.instashortlist.backend.controller;

import com.instashortlist.backend.dto.UpdateStatusRequest;
import com.instashortlist.backend.model.CandidateStep;
import com.instashortlist.backend.service.CandidateStepService;
import com.instashortlist.backend.repository.CandidateStepRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/candidate-steps")
public class CandidateStepController {

    @Autowired
    private CandidateStepService stepService;

    @Autowired
    private CandidateStepRepository candidateStepRepository;

    // ✅ PATCH endpoint (existing)
    @PatchMapping("/{stepId}/status")
    public Mono<CandidateStep> updateStatus(
            @PathVariable Long stepId,
            @RequestBody UpdateStatusRequest request) {
        return stepService.updateStepStatus(stepId, request);
    }

    // ✅ GET endpoint for steps of a candidate
    @GetMapping("/by-candidate/{candidateId}")
    public Flux<CandidateStep> getStepsByCandidateId(@PathVariable Long candidateId) {
        return candidateStepRepository.findByCandidateIdOrderByStepOrderAsc(candidateId);
    }
}
