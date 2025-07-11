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

    @Column("step_name")
    private String stepName;
    
}
