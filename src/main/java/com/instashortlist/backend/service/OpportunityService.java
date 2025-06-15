package com.instashortlist.backend.service;

import com.instashortlist.backend.model.Opportunity;
import com.instashortlist.backend.repository.OpportunityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OpportunityService {

    @Autowired
    private OpportunityRepository opportunityRepository;

    public Flux<Opportunity> getAll() {
        return opportunityRepository.findAll();
    }

    public Mono<Opportunity> getById(Long id) {
        return opportunityRepository.findById(id);
    }

    public Mono<Opportunity> create(Opportunity opportunity) {
        return opportunityRepository.save(opportunity);
    }

    public Mono<Opportunity> update(Long id, Opportunity updated) {
        return opportunityRepository.findById(id)
                .flatMap(existing -> {
                    existing.setCompany(updated.getCompany());
                    existing.setRole(updated.getRole());
                    existing.setLocation(updated.getLocation());
                    existing.setDescription(updated.getDescription());
                    return opportunityRepository.save(existing);
                });
    }

    public Mono<Void> delete(Long id) {
        return opportunityRepository.findById(id)
                .flatMap(opportunityRepository::delete);
    }
}
