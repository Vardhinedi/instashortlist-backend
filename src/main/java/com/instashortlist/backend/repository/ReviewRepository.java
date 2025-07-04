package com.instashortlist.backend.repository;

import com.instashortlist.backend.model.Review;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends ReactiveCrudRepository<Review, Long> {
    // You can add custom query methods here if needed
}
