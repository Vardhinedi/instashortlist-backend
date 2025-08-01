package com.instashortlist.backend.service;

import com.instashortlist.backend.model.CandidateStep;
import com.instashortlist.backend.model.Review;
import com.instashortlist.backend.repository.CandidateStepRepository;
import com.instashortlist.backend.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CandidateStepRepository candidateStepRepository;

    public Flux<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Mono<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    public Mono<Review> createReview(Review review) {
        review.setCreatedAt(LocalDateTime.now());
        return reviewRepository.save(review);
    }

    public Mono<Review> updateReview(Long id, Review updatedReview) {
        return reviewRepository.findById(id)
                .flatMap(existing -> {
                    existing.setStepId(updatedReview.getStepId());
                    existing.setReviewer(updatedReview.getReviewer());
                    existing.setFeedback(updatedReview.getFeedback());
                    existing.setScore(updatedReview.getScore());
                    existing.setCreatedAt(updatedReview.getCreatedAt());
                    return reviewRepository.save(existing);
                });
    }

    public Mono<Void> deleteReview(Long id) {
        return reviewRepository.deleteById(id);
    }

    public Flux<Review> getReviewsByStepId(Long stepId) {
        return reviewRepository.findAll()
                .filter(review -> review.getStepId().equals(stepId));
    }

    // ✅ Create and link review to stepId, update step status
    public Mono<Review> createReviewForStep(Long stepId, Review review) {
        review.setStepId(stepId);
        review.setCreatedAt(LocalDateTime.now());

        return reviewRepository.save(review)
                .flatMap(savedReview ->
                        candidateStepRepository.findById(stepId)
                                .flatMap(step -> {
                                    if (review.getScore() != null && review.getScore() < 50) {
                                        step.setStatus("FAILED");
                                    } else {
                                        step.setStatus("PASSED");
                                    }
                                    step.setCompleted(true);
                                    return candidateStepRepository.save(step)
                                            .thenReturn(savedReview);
                                })
                );
    }
}
