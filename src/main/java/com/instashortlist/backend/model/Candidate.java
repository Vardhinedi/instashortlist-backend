package com.instashortlist.backend.model;

public class Candidate {
    private String name;
    private String email;
    private String jobRole;
    private boolean shortlisted;

    public Candidate(String name, String email, String jobRole, boolean shortlisted) {
        this.name = name;
        this.email = email;
        this.jobRole = jobRole;
        this.shortlisted = shortlisted;
    }

    // Getters
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getJobRole() { return jobRole; }
    public boolean isShortlisted() { return shortlisted; }
}
