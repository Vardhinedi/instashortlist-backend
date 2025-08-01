package com.instashortlist.backend.controller;

import com.instashortlist.backend.model.Review;
import com.instashortlist.backend.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public Flux<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @GetMapping("/{id}")
    public Mono<Review> getReviewById(@PathVariable Long id) {
        return reviewService.getReviewById(id);
    }

    @PostMapping
    public Mono<Review> createReview(@RequestBody Review review) {
        return reviewService.createReview(review);
    }

    @PutMapping("/{id}")
    public Mono<Review> updateReview(@PathVariable Long id, @RequestBody Review updatedReview) {
        return reviewService.updateReview(id, updatedReview);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteReview(@PathVariable Long id) {
        return reviewService.deleteReview(id);
    }

    @GetMapping("/step/{stepId}")
    public Flux<Review> getReviewsByStepId(@PathVariable Long stepId) {
        return reviewService.getReviewsByStepId(stepId);
    }

    // ✅ FINAL: create review for a step
    @PostMapping("/step/{stepId}")
    public Mono<ResponseEntity<Review>> createReviewForStep(@PathVariable Long stepId, @RequestBody Review review) {
        return reviewService.createReviewForStep(stepId, review)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
}
