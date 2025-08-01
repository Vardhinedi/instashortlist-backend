package com.instashortlist.backend.repository;

import com.instashortlist.backend.model.Job;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends ReactiveCrudRepository<Job, Long> {
}
