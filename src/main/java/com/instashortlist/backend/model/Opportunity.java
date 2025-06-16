package com.instashortlist.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("isl_opportunities")
public class Opportunity {
    @Id
    private Long id;
    private String company;
    private String role;
    private String location;
    private String description;
}
