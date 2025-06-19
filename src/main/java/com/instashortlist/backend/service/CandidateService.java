package com.instashortlist.backend.service;

import com.instashortlist.backend.model.Candidate;
import com.instashortlist.backend.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    public Flux<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    public Mono<Candidate> getCandidateById(Long id) {
        return candidateRepository.findById(id);
    }

    public Mono<Candidate> createCandidate(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    public Mono<Candidate> updateCandidate(Long id, Candidate updated) {
        return candidateRepository.findById(id)
                .flatMap(existing -> {
                    existing.setName(updated.getName());
                    existing.setEmail(updated.getEmail());
                    existing.setPhone(updated.getPhone());
                    existing.setRole(updated.getRole());
                    existing.setAppliedRole(updated.getAppliedRole());
                    existing.setDepartment(updated.getDepartment());
                    existing.setEmploymentType(updated.getEmploymentType());
                    existing.setWorkType(updated.getWorkType());
                    existing.setAppliedDate(updated.getAppliedDate());
                    existing.setStatus(updated.getStatus());
                    existing.setScore(updated.getScore());
                    existing.setPosition(updated.getPosition());
                    existing.setExperience(updated.getExperience());
                    existing.setAttachments(updated.getAttachments());
                    existing.setResumeUrl(updated.getResumeUrl());
                    existing.setJobId(updated.getJobId());
                    return candidateRepository.save(existing);
                });
    }

    public Mono<Void> deleteCandidate(Long id) {
        return candidateRepository.findById(id)
                .flatMap(candidateRepository::delete);
    }
}
