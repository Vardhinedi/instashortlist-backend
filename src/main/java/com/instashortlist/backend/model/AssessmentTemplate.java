package com.instashortlist.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("isl_assessment_templates")
public class AssessmentTemplate {

    @Id
    private Long id;

    private String role;

    @Column("step_order")
    private Integer stepOrder;

    private String question;

    private String options; // JSON string

    @Column("correct_answer")
    private String correctAnswer;

    private String type;
}
