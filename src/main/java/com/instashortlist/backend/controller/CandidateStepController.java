package com.instashortlist.backend.controller;

import com.instashortlist.backend.dto.CandidateStepsResponseDTO;
import com.instashortlist.backend.dto.UpdateStatusRequest;
import com.instashortlist.backend.model.CandidateStep;
import com.instashortlist.backend.repository.CandidateStepRepository;
import com.instashortlist.backend.service.CandidateStepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/candidate-steps")
@CrossOrigin(origins = "*")
public class CandidateStepController {

    @Autowired
    private CandidateStepService stepService;

    @Autowired
    private CandidateStepRepository candidateStepRepository;

    // ✅ PATCH: Update status of a step
    @PatchMapping("/{stepId}/status")
    public Mono<CandidateStep> updateStatus(
            @PathVariable Long stepId,
            @RequestBody UpdateStatusRequest request) {
        return stepService.updateStepStatus(stepId, request);
    }

    // ✅ CLEAN + UPDATED: Return step + score + avg in one DTO
    @GetMapping("/by-candidate/{candidateId}")
    public Mono<CandidateStepsResponseDTO> getStepsWithAverageScore(@PathVariable Long candidateId) {
        return stepService.getStepsAndAverageScore(candidateId);
    }
}
