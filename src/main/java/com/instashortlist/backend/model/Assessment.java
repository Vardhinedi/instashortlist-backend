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

    private String question;

    private String options; // store as JSON string

    @Column("correct_answer")
    private String correctAnswer;

    @Column("step_order")
    private Integer stepOrder;

    private String type; // optional
}
