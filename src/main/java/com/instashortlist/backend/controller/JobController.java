package com.instashortlist.backend.controller;

import com.instashortlist.backend.model.Job;
import com.instashortlist.backend.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping
    public Flux<Job> getAllJobs() {
        return jobService.getAllJobs();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Job>> getJobById(@PathVariable Long id) {
        return jobService.getJobById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Job>> createJob(@RequestBody Job job) {
        return jobService.createJob(job)
                .map(savedJob -> ResponseEntity.status(201).body(savedJob));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Job>> updateJob(@PathVariable Long id, @RequestBody Job job) {
        return jobService.updateJob(id, job)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteJob(@PathVariable Long id) {
        return jobService.deleteJob(id)
                .thenReturn(ResponseEntity.noContent().<Void>build());
    }
}
