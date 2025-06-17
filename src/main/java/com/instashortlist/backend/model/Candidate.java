package com.instashortlist.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@Table("isl_candidates")
public class Candidate {

    @Id
    private Long id;

    private String name;
    private String email;

    @Column("phone")
    private String phone;

    @Column("role")
    private String role;

    @Column("applied_role")
    private String appliedRole;

    @Column("department")
    private String department;

    @Column("employment_type")
    private String employmentType;

    @Column("work_type")
    private String workType;

    @Column("applied_date")
    private LocalDate appliedDate;

    @Column("status")
    private String status;

    @Column("score")
    private Integer score;

    @Column("position")
    private String position;

    @Column("experience")
    private Integer experience;

    @Column("attachments")
    private String attachments; // Comma-separated string: "Resume,ID"

    @Column("resume_url")
    private String resumeUrl;

    @Column("job_id")
    private Long jobId; // Foreign key to isl_jobs
}
