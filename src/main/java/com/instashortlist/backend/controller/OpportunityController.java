package com.instashortlist.backend.controller;

import com.instashortlist.backend.model.Opportunity;
import com.instashortlist.backend.repository.OpportunityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/opportunities")
public class OpportunityController {

    @Autowired
    private OpportunityRepository repository;

    // ✅ Get all opportunities
    @GetMapping
    public Flux<Opportunity> getAll() {
        return repository.findAll();
    }

    // ✅ Get opportunity by ID
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Opportunity>> getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // ✅ Create a new opportunity
    @PostMapping
    public Mono<Opportunity> create(@RequestBody Opportunity opportunity) {
        return repository.save(opportunity);
    }

    // ✅ Update an existing opportunity
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Opportunity>> update(@PathVariable Long id, @RequestBody Opportunity updated) {
        return repository.findById(id)
                .flatMap(existing -> {
                    existing.setCompany(updated.getCompany());
                    existing.setRole(updated.getRole());
                    existing.setLocation(updated.getLocation());
                    existing.setDescription(updated.getDescription());
                    return repository.save(existing);
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // ✅ Delete an opportunity (corrected return type)
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable Long id) {
        return repository.findById(id)
                .flatMap(existing ->
                        repository.delete(existing)
                                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
