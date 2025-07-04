package com.instashortlist.backend.controller;

import com.instashortlist.backend.model.Review;
import com.instashortlist.backend.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/reviews")
@CrossOrigin(origins = "*")  // Optional: You can restrict this as needed
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // 🔹 Get all reviews
    @GetMapping
    public Flux<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }

    // 🔹 Get a review by ID
    @GetMapping("/{id}")
    public Mono<Review> getReviewById(@PathVariable Long id) {
        return reviewService.getReviewById(id);
    }

    // 🔹 Create a new review
    @PostMapping
    public Mono<Review> createReview(@RequestBody Review review) {
        return reviewService.createReview(review);
    }

    // 🔹 Update a review by ID
    @PutMapping("/{id}")
    public Mono<Review> updateReview(@PathVariable Long id, @RequestBody Review updatedReview) {
        return reviewService.updateReview(id, updatedReview);
    }

    // 🔹 Delete a review by ID
    @DeleteMapping("/{id}")
    public Mono<Void> deleteReview(@PathVariable Long id) {
        return reviewService.deleteReview(id);
    }

    // ✅ NEW: Get all reviews for a specific candidate step
    @GetMapping("/step/{stepId}")
    public Flux<Review> getReviewsByStepId(@PathVariable Long stepId) {
        return reviewService.getReviewsByStepId(stepId);
    }

    // ✅ NEW: Create a review and update step status accordingly
    @PostMapping("/step/{stepId}")
    public Mono<Review> createReviewForStep(@PathVariable Long stepId, @RequestBody Review review) {
        return reviewService.createReviewForStep(stepId, review);
    }
}
