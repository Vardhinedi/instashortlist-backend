package com.instashortlist.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateStepWithReviewDTO {
    private Long id;
    private Long candidateId;
    private Long assessmentId;
    private Integer stepOrder;
    private String stepName;
    private String status;
    private Boolean completed;
    private Integer score; // Nullable if no review
}
