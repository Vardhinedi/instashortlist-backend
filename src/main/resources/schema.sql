-- Create applications table
CREATE TABLE IF NOT EXISTS isl_applications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    email VARCHAR(255),
    job_role VARCHAR(255),
    matched_skills VARCHAR(1024),
    missing_skills VARCHAR(1024),
    match_score INT,
    status VARCHAR(255),
    reason VARCHAR(1024),
    resume_path VARCHAR(255),
    created_at TIMESTAMP
      
);

-- Create jobs table (Updated)
CREATE TABLE IF NOT EXISTS isl_jobs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    role VARCHAR(255),
    location VARCHAR(255),
    description TEXT,
    salary_min INT,
    salary_max INT,
    type VARCHAR(100),
    level VARCHAR(100),
    is_active BOOLEAN DEFAULT TRUE,
    applicants INT DEFAULT 0
);

-- Create opportunities table
CREATE TABLE IF NOT EXISTS isl_opportunities (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    company VARCHAR(255),
    role VARCHAR(255),
    location VARCHAR(255),
    description TEXT
);

-- Create users table
CREATE TABLE IF NOT EXISTS isl_users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255),
    role VARCHAR(100),
    username VARCHAR(255) UNIQUE,
    password VARCHAR(255)
);

-- Create candidates table
CREATE TABLE IF NOT EXISTS isl_candidates (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    resume_url TEXT,
    job_id BIGINT,
    FOREIGN KEY (job_id) REFERENCES isl_jobs(id) ON DELETE CASCADE
);
