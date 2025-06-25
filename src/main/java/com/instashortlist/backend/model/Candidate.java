package com.instashortlist.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.nio.ByteBuffer;
import java.time.LocalDate;

@Data
@Table("isl_candidates")
public class Candidate {

    @Id
    private Long id;

    @NotNull(message = "Name cannot be null")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Phone cannot be null")
    @Size(min = 10, max = 20, message = "Phone must be between 10 and 20 characters")
    @Column("phone")
    private String phone;

    @NotNull(message = "Role cannot be null")
    @Size(min = 2, max = 100, message = "Role must be between 2 and 100 characters")
    @Column("role")
    private String role;

    @NotNull(message = "Applied role cannot be null")
    @Size(min = 2, max = 100, message = "Applied role must be between 2 and 100 characters")
    @Column("applied_role")
    private String appliedRole;

    @NotNull(message = "Department cannot be null")
    @Size(min = 2, max = 100, message = "Department must be between 2 and 100 characters")
    @Column("department")
    private String department;

    @NotNull(message = "Employment type cannot be null")
    @Size(min = 2, max = 100, message = "Employment type must be between 2 and 100 characters")
    @Column("employment_type")
    private String employmentType;

    @NotNull(message = "Work type cannot be null")
    @Size(min = 2, max = 100, message = "Work type must be between 2 and 100 characters")
    @Column("work_type")
    private String workType;

    @Column("applied_date")
    private LocalDate appliedDate;

    @Transient
    private String appliedDateStr;

    @NotNull(message = "Status cannot be null")
    @Size(min = 2, max = 100, message = "Status must be between 2 and 100 characters")
    @Column("status")
    private String status;

    @Column("score")
    private Integer score;

    @NotNull(message = "Position cannot be null")
    @Size(min = 2, max = 100, message = "Position must be between 2 and 100 characters")
    @Column("position")
    private String position;

    @Column("experience")
    private Integer experience;

    @Column("attachments")
    private ByteBuffer attachments; // ✅ Updated from byte[] to ByteBuffer

    @Column("resume_url")
    private String resumeUrl;

    @NotNull(message = "Job ID cannot be null")
    @Column("job_id")
    private Long jobId;
}
