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
    private String role;

    @Column("applied_role")
    private String appliedRole;

    private String department;

    @Column("employment_type")
    private String employmentType;

    @Column("work_type")
    private String workType;

    @Column("applied_date")
    private LocalDate appliedDate;

    private String status;
    private String email;
    private String position;
    private String attachments;
    private Integer score;
    private String phone;
    private Integer experience;

    @Column("resume_url")
    private String resumeUrl;

    @Column("job_id")
    private Long jobId;
}
