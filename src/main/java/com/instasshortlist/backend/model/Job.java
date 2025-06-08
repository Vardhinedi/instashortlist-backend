package com.instashortlist.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("jobs")
public class Job {
    @Id
    private Long id;
    private String title;
    private String description;
}
