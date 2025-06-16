package com.instashortlist.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("isl_applications")
public class Application {
    @Id
    private Long id;
    private String name;
    private String email;
    private String jobRole;
    private String matchedSkills;
    private String missingSkills;
    private int matchScore;
    private String status;
    private String reason;
    private String resumePath;             // ✅ new field
    private LocalDateTime createdAt;
}
