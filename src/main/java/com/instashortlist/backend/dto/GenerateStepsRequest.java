package com.instashortlist.backend.dto;

import lombok.Data;

@Data
public class GenerateStepsRequest {
    private Long candidateId;
    private Long jobId;
}
