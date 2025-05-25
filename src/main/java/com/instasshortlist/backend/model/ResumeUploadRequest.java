package com.instashortlist.backend.model;

public class ResumeUploadRequest {
    private String name;
    private String email;
    private String jobRole;

    // Getters and Setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getJobRole() {
        return jobRole;
    }
    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }
}
