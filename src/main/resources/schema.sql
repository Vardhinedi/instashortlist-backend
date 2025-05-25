CREATE TABLE applications (
    id IDENTITY PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255),
    job_role VARCHAR(255),
    matched_skills VARCHAR(1024),
    missing_skills VARCHAR(1024),
    match_score INT,
    status VARCHAR(255),
    reason VARCHAR(1024),
    created_at TIMESTAMP
);