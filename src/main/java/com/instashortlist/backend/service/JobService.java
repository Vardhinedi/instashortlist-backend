package com.instashortlist.backend.service;

import com.instashortlist.backend.model.Job;
import com.instashortlist.backend.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    public Flux<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Mono<Job> getJobById(Long id) {
        return jobRepository.findById(id);
    }

    public Mono<Job> createJob(Job job) {
        // Set defaults if not provided
        if (job.getIsActive() == null) job.setIsActive(true);
        if (job.getApplicants() == null) job.setApplicants(0);
        return jobRepository.save(job);
    }

    public Mono<Job> updateJob(Long id, Job updatedJob) {
        return jobRepository.findById(id)
                .flatMap(existing -> {
                    existing.setTitle(updatedJob.getTitle());
                    existing.setDescription(updatedJob.getDescription());
                    existing.setRole(updatedJob.getRole());
                    existing.setLocation(updatedJob.getLocation());
                    existing.setType(updatedJob.getType());
                    existing.setLevel(updatedJob.getLevel());
                    existing.setSalaryMin(updatedJob.getSalaryMin());
                    existing.setSalaryMax(updatedJob.getSalaryMax());
                    existing.setIsActive(updatedJob.getIsActive());
                    existing.setApplicants(updatedJob.getApplicants());
                    return jobRepository.save(existing);
                });
    }

    public Mono<Void> deleteJob(Long id) {
        return jobRepository.findById(id)
                .flatMap(jobRepository::delete);
    }
}
