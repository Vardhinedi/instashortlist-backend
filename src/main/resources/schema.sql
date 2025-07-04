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

-- Updated candidates table
CREATE TABLE IF NOT EXISTS isl_candidates (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    role VARCHAR(100),
    applied_role VARCHAR(100),
    department VARCHAR(100),
    employment_type VARCHAR(100),
    work_type VARCHAR(100),
    applied_date DATE,
    status VARCHAR(100),
    email VARCHAR(100),
    position VARCHAR(100),
    attachments LONGBLOB,
    score INT,
    phone VARCHAR(20),
    experience INT,
    resume_url TEXT,
    job_id BIGINT,
    FOREIGN KEY (job_id) REFERENCES isl_jobs(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS isl_assessments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    job_id BIGINT NOT NULL,
    question TEXT,
    options TEXT,                 -- store JSON string of options
    correct_answer TEXT,
    type VARCHAR(100),
    step_order INT,
    FOREIGN KEY (job_id) REFERENCES isl_jobs(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS isl_candidate_steps (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    candidate_id BIGINT UNSIGNED,
    assessment_id BIGINT,
    step_order INT,
    step_name VARCHAR(255),
    status VARCHAR(100),
    completed BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (candidate_id) REFERENCES isl_candidates(id) ON DELETE CASCADE,
    FOREIGN KEY (assessment_id) REFERENCES isl_assessments(id) ON DELETE CASCADE
);

-- STEP 3: isl_reviews table
CREATE TABLE IF NOT EXISTS isl_reviews (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    step_id BIGINT UNSIGNED,
    reviewer VARCHAR(255),
    feedback TEXT,
    score INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (step_id) REFERENCES isl_candidate_steps(id) ON DELETE CASCADE
);

