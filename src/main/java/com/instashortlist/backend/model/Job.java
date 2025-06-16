package com.instashortlist.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("isl_jobs")
public class Job {
    @Id
    private Long id;

    private String title;
    private String role;              // 🆕 added
    private String location;
    private String description;

    @Column("salary_min")
    private Integer salaryMin;

    @Column("salary_max")
    private Integer salaryMax;

    private String type;              // e.g. Full-time
    private String level;             // e.g. Mid-level

    @Column("is_active")
    private Boolean isActive = true;
    private Integer applicants = 0;
}
