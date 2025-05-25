package com.instashortlist.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Table("applications")
public class Application {
    @Id
    private Long id;
    private String name;
    private String email;
    private String jobRole;
    private List<String> matchedSkills;
    private List<String> missingSkills;
    private int matchScore;
    private String status;
    private String reason;
    private LocalDateTime createdAt;
}
