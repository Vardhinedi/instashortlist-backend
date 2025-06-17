package com.instashortlist.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("isl_candidates")
public class Candidate {

    @Id
    private Long id;

    private String name;
    private String email;

    @Column("resume_url")
    private String resumeUrl;

    @Column("job_id")  // Foreign key to Job
    private Long jobId;
}
