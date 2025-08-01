package com.instashortlist.backend.repository;

import com.instashortlist.backend.model.Application;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends ReactiveCrudRepository<Application, Long> {
}
