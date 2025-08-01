package com.instashortlist.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("isl_reviews")
public class Review {

    @Id
    private Long id;

    @Column("step_id")
    private Long stepId; // Step ID will be set from path variable

    private String reviewer;
    private String feedback;
    private Integer score;

    @Column("created_at")
    private LocalDateTime createdAt;
}
