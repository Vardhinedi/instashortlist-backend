package com.instashortlist.backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class JobRequest {
    private String title;
    private String role;
    private String location;
    private String description;
    private Integer salaryMin;
    private Integer salaryMax;
    private String type;
    private String level;
    private Boolean isActive;
    private List<Long> assessmentTemplateIds; // 🆕 selected templates
}
