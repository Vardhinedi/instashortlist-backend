package com.instashortlist.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateStepsResponseDTO {
    private List<CandidateStepWithReviewDTO> steps;
}
