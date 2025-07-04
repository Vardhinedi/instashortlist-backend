package com.instashortlist.backend.dto;

import lombok.Data;

@Data
public class UpdateStatusRequest {
    private String status;         // e.g., "PASSED", "FAILED"
    private Boolean completed;     // Optional: true/false
}
