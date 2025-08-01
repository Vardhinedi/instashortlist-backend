package com.instashortlist.backend.dto;

import lombok.Data;

@Data
public class CandidateResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String role;
    private String appliedRole;
    private String department;
    private String employmentType;
    private String workType;
    private String appliedDate;
    private String status;
    private Integer score;
    private String position;
    private Integer experience;
    private String resumeUrl;
    private Long jobId;
    private AttachmentDTO attachment;
}
