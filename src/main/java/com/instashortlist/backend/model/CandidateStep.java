package com.instashortlist.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("isl_candidate_steps")
public class CandidateStep {

    @Id
    private Long id;

    @Column("candidate_id")
    private Long candidateId;

    @Column("assessment_id")
    private Long assessmentId;

    @Column("step_order")
    private Integer stepOrder;

    @Column("step_name")
    private String stepName;

    private String status;

    private Boolean completed;
}
