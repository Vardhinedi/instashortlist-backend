package com.instashortlist.backend.controller;

import com.instashortlist.backend.model.Candidate;
import com.instashortlist.backend.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @GetMapping
    public Flux<Candidate> getAll() {
        return candidateService.getAllCandidates();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Candidate>> getById(@PathVariable Long id) {
        return candidateService.getCandidateById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Candidate>> create(@RequestBody Candidate candidate) {
        return candidateService.createCandidate(candidate)
                .map(saved -> ResponseEntity.status(201).body(saved));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Candidate>> update(@PathVariable Long id, @RequestBody Candidate updated) {
        return candidateService.updateCandidate(id, updated)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable Long id) {
        return candidateService.deleteCandidate(id)
                .thenReturn(ResponseEntity.noContent().<Void>build());
    }
}
