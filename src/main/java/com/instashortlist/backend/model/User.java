package com.instashortlist.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("isl_users")
public class User {
    @Id
    private Long id;
    private String name;
    private String email;
    private String role;        // e.g. "admin", "recruiter", "applicant"
    private String username;
    private String password;
}
