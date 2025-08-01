package com.instashortlist.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("isl_assessments")
public class Assessment {

    @Id
    private Long id;

    @Column("job_id")
    private Long jobId;

    @Column("step_order")
    private Integer stepOrder;

    @Column("step_name")
    private String stepName;

    private String mode; // e.g. MCQ, Video, Whiteboard, etc.

    @Column("passing_criteria")
    private String passingCriteria;
}
